package randomMindustry;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.BuildVisibility;

public class BlockMapper {
    public static void init() {
        Seq<Block> blocks = Vars.content.blocks().copy();
//        blocks.shuffle();
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block) {
        if (block instanceof GenericCrafter) modifyCrafter((GenericCrafter) block);
    }

    public static void modifyCrafter(GenericCrafter block) {
        Item item = ResourceMapper.getLocked("craft");
        if (item == null) item = ResourceMapper.getAll();
        int count = ResourceMapper.getRandomInt(10)+1;
        block.outputItems = new ItemStack[]{new ItemStack(
                item, count
        )};
    }
}
