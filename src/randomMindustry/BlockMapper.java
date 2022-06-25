package randomMindustry;

import arc.math.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.Consume;

import static mindustry.Vars.*;

public class BlockMapper{
    public static void init(){
        Seq<Block> blocks = content.blocks().copy();
        shuffle(blocks, Main.rand);
        blocks.each(BlockMapper::modify);
    }

    public static <T> void shuffle(Seq<T> seq, Rand rand){
        T[] items = seq.items;
        for(int i = seq.size - 1; i >= 0; i--){
            int ii = rand.random(i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    public static void modify(Block block){
        TechTree.TechNode node = block.techNode;
        if(node == null) return;
        while (node.parent != null) node = node.parent;
        if(Planets.serpulo.techTree != node) return;
        if(block instanceof GenericCrafter){
            modifyCrafter((GenericCrafter)block);
        }else if (block == Blocks.mechanicalDrill){
            modifyMechanicalDrill(block);
        }else{
            modifyBlock(block);
        }
    }

    public static void modifyCrafter(GenericCrafter block){
        Item item = ResourceMapper.getGenericCrafterOut();
        if (item == null) item = ResourceMapper.getRandomItem(false);
        int count = ResourceMapper.getRandomInt(10)+1;
        block.outputItems = new ItemStack[]{new ItemStack(
                item, count
        )};
        int tier = ResourceMapper.getTierOfItem(item);
        Consume[] save = block.consumers;
        block.outputLiquids = new LiquidStack[0];
        block.outputsLiquid = false;
        block.consumers = new Consume[0];
        for (Consume consume : save) block.removeConsumer(consume);
        block.consumeItems(ResourceMapper.getRandomItemStacks(tier, 3, 10, true));
        block.requirements = ResourceMapper.getRandomItemStacks(tier, 5, block.health / 2, true);
        block.init();
    }

    public static void modifyBlock(Block block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(5)+1, 5, block.health / 2, true);
    }

    public static void modifyMechanicalDrill(Block block) {
        block.requirements = ResourceMapper.getRandomItemStacks(1, 2, block.health / 2, true);
    }
}
