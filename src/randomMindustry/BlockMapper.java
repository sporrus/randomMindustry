package randomMindustry;

import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

import static mindustry.Vars.*;

public class BlockMapper {
    public static void init() {
        Seq<Block> blocks = content.blocks().copy();
        Main.shuffle(blocks);
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block) {
        Seq<Category> cats = new Seq<>();
        cats.addAll(Category.all);
        block.category = cats.random(Main.rand);
        if (!Main.getRoot(block).contains(Planets.serpulo)) return;
        if (block instanceof GenericCrafter) {
            modifyCrafter((GenericCrafter) block);
        } else if (block instanceof Drill) {
            modifyDrill((Drill) block);
        } else if (block instanceof Conveyor) {
            modifyConveyor((Conveyor) block);
        } else if (block instanceof Wall) {
            modifyWall((Wall) block);
        } else {
            modifyBlock(block);
        }
    }

    public static void modifyCrafter(GenericCrafter block) {
        Item item = ResourceMapper.getGenericCrafterOut();
        if (item == null) item = ResourceMapper.getRandomItem(false);
        int count = ResourceMapper.getRandomInt(10) + 1;
        block.outputItems = new ItemStack[]{new ItemStack(
                item, count
        )};
        int tier = ResourceMapper.getTierOfItem(item);
        Consume[] save = block.consumers;
        block.consumers = new Consume[0];
        for (Consume consume : save) block.removeConsumer(consume);
        block.consumeItems(ResourceMapper.getRandomItemStacks(tier, 3, 10, 1, true));
        block.requirements = ResourceMapper.getRandomItemStacks(tier, 5, block.health / 2, 5, true);
        block.init();
    }

    public static void modifyConveyor(Conveyor block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 3, block.health / 4, 1, true);
    }

    public static void modifyBlock(Block block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 5, block.health / 2, 5, true);
    }

    public static void modifyDrill(Drill block) {
        int localTier = block.tier * 2 - 3;
        block.requirements = ResourceMapper.getRandomItemStacks(localTier, 5, block.health / 2, 5, true);
    }

    public static void modifyWall(Wall block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 5, block.size * 10, 5, true);
    }
}
