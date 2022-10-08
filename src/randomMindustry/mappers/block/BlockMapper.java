package randomMindustry.mappers.block;

import arc.struct.Seq;
import mindustry.world.Block;
import randomMindustry.SyncedRand;
import randomMindustry.mappers.Mapper;
import randomMindustry.mappers.block.blocks.RandomBlock;
import randomMindustry.mappers.block.blocks.RandomCrafter;
import randomMindustry.mappers.block.blocks.RandomOre;
import randomMindustry.mappers.block.blocks.RandomWall;
import randomMindustry.mappers.item.ItemMapper;

public class BlockMapper implements Mapper {
    public static final Seq<RandomBlock> generatedBlocks = new Seq<>();
    public static final SyncedRand r = new SyncedRand();

    public void editContent() {
        generatedBlocks.each(RandomBlock::edit);
    }

    @Override
    public void generateContent() {
        for (int i = 0; i < ItemMapper.getDrillItems(); i++)
            generatedBlocks.add(new RandomOre("random-ore-" + i));
        for (int i = 0; i < ItemMapper.getCraftItems(); i++)
            generatedBlocks.add(new RandomCrafter("random-crafter-" + i));
        for (int i = 0; i < 12; i++)
            generatedBlocks.add(new RandomWall("random-wall-" + i));
    }

    public static boolean generated(Block block) {
        return block instanceof RandomBlock;
    }
}
