package randomMindustry.util;

import arc.func.*;
import arc.graphics.g2d.TextureRegion;
import arc.struct.*;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.*;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.*;

import static mindustry.Vars.content;

public class Util {
    public static void removeAllConsumers(Block block) {
        removeConsumers(block, (consume -> true));
    }

    public static void removeConsumers(Block block, Boolf<Consume> predicate) {
        Seq<Consume> remove = new Seq<>(block.consumers).select(predicate);
        Seq<Consume> save = new Seq<>(block.consumers);
        save.removeAll(remove);
        block.consumers = new Consume[0];
        for (Consume consume : remove) block.removeConsumer(consume);
        for (Consume consume : save) block.consume(consume);
    }

    public static Seq<Block> findRecipeOut(Item item) {
        return content.blocks().select((block -> {
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
        return content.blocks().select((block -> {
            if (new Seq<>(block.requirements).contains(itemStack -> itemStack.item == item)) return true;
            for (Consume consume : block.consumers) {
                if (consume instanceof ConsumeItems consumeItems) {
                    if (new Seq<>(consumeItems.items).contains(itemStack -> itemStack.item == item)) return true;
                }
            }
            return false;
        }));
    }
}
