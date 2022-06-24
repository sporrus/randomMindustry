package randomMindustry;

import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

public class BlockMapper{
    // TODO: separate for both planets?
    static Seq<Block> tier1, tier2, tier3, tier4, tier5;
    
    public static void init(){
        Seq<Block> blocks = content.blocks().copy();
        
        tier1 = new Seq<>(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 1));
        tier2 = new Seq<>(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 2));
        tier3 = new Seq<>(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 3));
        tier4 = new Seq<>(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 4));
        tier5 = new Seq<>(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 5));
        
        blocks.shuffle();
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block){
        // ores dont have nodes so im putting this on the front
        if (block instanceof OreBlock) modifyOre((OreBlock)block);
        
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
    
    public static void modifyOre(OreBlock block) {
        switch(block.itemDrop.hardness) {
            case 1:
                Block t1 = tier1.random();
                tier1.remove(t1);
                block.itemDrop = t1.itemDrop;
                break;
            case 2:
                Block t2 = tier2.random();
                tier2.remove(t2);
                block.itemDrop = t2.itemDrop;
                break;
            case 3:
                Block t3 = tier3.random();
                tier3.remove(t3);
                block.itemDrop = t3.itemDrop;
                break;
            case 4:
                Block t4 = tier4.random();
                tier4.remove(t4);
                block.itemDrop = t4.itemDrop;
                break;
            case 5:
                Block t5 = tier5.random();
                tier5.remove(t5);
                block.itemDrop = t5.itemDrop;
                break;
        }
    }
}
