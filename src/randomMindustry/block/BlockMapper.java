package randomMindustry.block;

import arc.math.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import randomMindustry.*;
import randomMindustry.block.creators.*;
import randomMindustry.item.*;
import randomMindustry.texture.*;

public class BlockMapper {
    public static final Seq<Block> generatedBlocks = new Seq<>();
    public static final SyncedRand r = new SyncedRand();
    public static final BlockCreator
            wallBlockCreator = new WallBlockCreator(),
            crafterBlockCreator = new CrafterBlockCreator();
    public static final Seq<BlockCreator> creators = new Seq<>(new BlockCreator[]{
        wallBlockCreator, crafterBlockCreator
    });

    public static void editContent() {
        generatedBlocks.each((b) -> {
            for (BlockCreator creator : creators) {
                if (!creator.has(b)) continue;
                creator.edit(b);
                b.init();
                break;
            }
        });
    }

    public static void generateContent() {
        for (int i = 0; i < ItemMapper.getCraftItems(); i++)
            generatedBlocks.add(crafterBlockCreator.create("random-crafter-" + i));
        for (int i = 0; i < 12; i++)
            generatedBlocks.add(wallBlockCreator.create("random-wall-" + i));
    }

    public static boolean generated(Block block) {
        return generatedBlocks.contains(block);
    }
}
