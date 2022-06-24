package randomMindustry;

import arc.struct.*;
import mindustry.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

public class BlockMapper {
    public static void init() {
        Seq<Block> blocks = Vars.content.blocks().copy();
//        blocks.shuffle();
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block) {
        if (block instanceof GenericCrafter && block.supportsEnv(17)) modifyCrafter((GenericCrafter) block);
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
