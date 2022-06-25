package randomMindustry;

import arc.struct.*;
import arc.util.Log;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.blocks.storage.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class BlockMapper {
    public static void init() {
        Seq<Block> blocks = content.blocks().copy();
        Main.shuffle(blocks, Main.rand);
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block) {
        if (!Main.getRoot(block).contains(Planets.serpulo)) return;
        if (block instanceof GenericCrafter) {
            modifyCrafter((GenericCrafter) block);
        } else if (block == Blocks.mechanicalDrill) {
            modifyMechanicalDrill(block);
        } else if (block instanceof CoreBlock) {
            modifyCore((CoreBlock) block);
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

    public static void modifyBlock(Block block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(5) + 1, 5, block.health / 2, 5, true);
    }

    public static void modifyMechanicalDrill(Block block) {
        block.requirements = ResourceMapper.getRandomItemStacks(1, 2, block.health / 2, 5, true);
    }
    
    // Suggested by Ilya246.
    public static void modifyCore(CoreBlock block){
        Seq<UnitType> coreUnits = content.units().select(u -> u.mineTier >= 1 && u.buildSpeed > 0 && (u.flying || u.canBoost));
        
        // TODO: Avoid rolling on the same unit? nah
        block.unitType = coreUnits.random(Main.rand);
    }
}
