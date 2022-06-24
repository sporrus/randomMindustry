package randomMindustry;

import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;

import static mindustry.Vars.*;

public class BlockMapper{
    public static void init(){
        Seq<Block> blocks = content.blocks().copy();
        blocks.shuffle();
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block){
        TechTree.TechNode node = block.techNode;
        if(node == null) return;
        while (node.parent != null) node = node.parent;
        if(Planets.serpulo.techTree != node) return;
        if(block instanceof GenericCrafter){
            modifyCrafter((GenericCrafter)block);
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
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getTierOfItem(item), 5, 1000, true);
    }

    public static void modifyBlock(Block block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(5)+1, 5, 1000, true);
    }
}
