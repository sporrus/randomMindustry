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
    static Seq<Item> tier1, tier2, tier3, tier4, tier5;
    
    public static void init(){
        Seq<Block> blocks = content.blocks().copy();
        
        tier1.addAll(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 1));
        tier2.addAll(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 2));
        tier3.addAll(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 3));
        tier4.addAll(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 4));
        tier5.addAll(blocks.select(b -> b instanceof OreBlock && b.itemDrop.hardness == 5));
        
        blocks.shuffle();
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block){
        // ores dont have nodes so im putting this on the front
        if (block instanceof OreBlock) modifyOre(block);
        
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
                Item i = tier1.random();
                tier1.remove(i);
                block.itemDrop = i;
                break;
            case 2:
                Item i = tier2.random();
                tier2.remove(i);
                block.itemDrop = i;
                break;
            case 3:
                Item i = tier3.random();
                tier3.remove(i);
                block.itemDrop = i;
                break;
            case 4:
                Item i = tier4.random();
                tier4.remove(i);
                block.itemDrop = i;
                break;
            case 5:
                Item i = tier5.random();
                tier5.remove(i);
                block.itemDrop = i;
                break;
        }
    }
}
