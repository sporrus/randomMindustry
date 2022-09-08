package randomMindustry.mappers.block;

import arc.struct.*;
import mindustry.world.*;
import randomMindustry.*;
import randomMindustry.mappers.Mapper;
import randomMindustry.mappers.block.creators.*;
import randomMindustry.mappers.item.*;

public class BlockMapper implements Mapper {
    public static final Seq<Block> generatedBlocks = new Seq<>();
    public static final SyncedRand r = new SyncedRand();
    public static final BlockCreator
            wallBlockCreator = new WallBlockCreator(),
            crafterBlockCreator = new CrafterBlockCreator(),
            oreBlockCreator = new OreBlockCreator();
    public static final Seq<BlockCreator> creators = new Seq<>(new BlockCreator[]{
            wallBlockCreator, crafterBlockCreator, oreBlockCreator
    });

    @Override
    public void editContent() {
        generatedBlocks.each((b) -> {
            for (BlockCreator creator : creators) {
                if (!creator.has(b)) continue;
                creator.edit(b);
                b.init();
                break;
            }
        });
    }

    @Override
    public void generateContent() {
        for (int i = 0; i < ItemMapper.getCraftItems(); i++)
            generatedBlocks.add(crafterBlockCreator.create("random-crafter-" + i));
        for (int i = 0; i < ItemMapper.getDrillItems(); i++)
            generatedBlocks.add(oreBlockCreator.create("random-ore-" + i));
        for (int i = 0; i < 12; i++)
            generatedBlocks.add(wallBlockCreator.create("random-wall-" + i));
    }

    public static boolean generated(Block block) {
        return generatedBlocks.contains(block);
    }
}
