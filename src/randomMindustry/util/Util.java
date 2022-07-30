package randomMindustry.util;

import arc.audio.Sound;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.type.*;
import mindustry.ui.Bar;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import randomMindustry.random.mappers.blocks.BlockMapper;
import randomMindustry.random.util.RandomUtil;

import java.lang.reflect.Field;

import static mindustry.Vars.content;

public class Util {
    public static void removeBars(Block block) {
        try {
            Field field = Block.class.getDeclaredField("barMap");
            field.setAccessible(true);
            ObjectMap<String, Func<Building, Bar>> map = (ObjectMap<String, Func<Building, Bar>>) field.get(block);
            map.clear();
        } catch (Exception e) {
            Log.err("Could not remove bars!", e);
        }
    }
    public static void removeAllConsumers(Block block) {
        removeConsumers(block, (consume -> true));
    }

    public static void removeConsumers(Block block, Boolf<Consume> predicate) {
        Seq<Consume> remove = new Seq<>(block.consumers).select(predicate);
        Seq<Consume> save = new Seq<>(block.consumers);
        save.removeAll(remove);
        block.consumers = new Consume[0];
        for (Consume consume : remove) block.removeConsumer(consume);
    }

    public static Seq<Block> findRecipeOut(Item item) {
        return BlockMapper.getSelectedBlocks().select((block -> {
            if (block instanceof GenericCrafter) {
                ItemStack[] items = ((GenericCrafter) block).outputItems;
                if (items == null) return false;
                for (ItemStack itemStack : items) if (itemStack.item == item) return true;
                return false;
            }
            return false;
        }));
    }

    public static TextureRegion getHardnessIcon(Block block) {
        if (block.itemDrop.hardness == 1) return UnitTypes.alpha.uiIcon;
        else if (block.itemDrop.hardness == 2) return Blocks.mechanicalDrill.uiIcon;
        else if (block.itemDrop.hardness == 3) return Blocks.pneumaticDrill.uiIcon;
        else if (block.itemDrop.hardness == 4) return Blocks.laserDrill.uiIcon;
        return Blocks.blastDrill.uiIcon;
    }

    public static Seq<Block> findHowToGet(Item item) {
        return findRecipeOut(item).addAll(content.blocks().select(block -> block.itemDrop == item));
    }

    public static Seq<Block> findRecipeIn(Item item) {
        return BlockMapper.getSelectedBlocks().select((block -> {
            if (new Seq<>(block.requirements).contains(itemStack -> itemStack.item == item)) return true;
            for (Consume consume : block.consumers) {
                if (consume instanceof ConsumeItems consumeItems) {
                    if (new Seq<>(consumeItems.items).contains(itemStack -> itemStack.item == item)) return true;
                }
            }
            return false;
        }));
    }

    public static void resetStats(Block block) {
//        block.stats = new Stats();
//        block.stats.intialized = false;
    }

    public static void updateStats(Block block) {
        if (block == Blocks.buildTower) return; // AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        block.stats = new Stats();
        block.init();
    }

    public static Sound generateSound() {
        int id = RandomUtil.getClientRand().random(0, 70);
        if (id == Sounds.getSoundId(Sounds.swish)) id = 71;
        return Sounds.getSound(id);
    }
}
