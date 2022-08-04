package randomMindustry.block;

import arc.math.*;
import arc.struct.*;
import randomMindustry.*;
import randomMindustry.item.*;

public class BlockMapper {
    public static final Seq<CustomBlock> generatedBlocks = new Seq<>();
    public static final int blockCount = 32;
    public static final Rand rand = new Rand();

    public static void editContent() {
        rand.setSeed(SeedManager.getSeed());
        generatedBlocks.each(CustomBlock::edit);
    }

    public static void generateContent() {
        for (int i = 0; i < blockCount; i++)
            generatedBlocks.add(new CustomBlock("random-block-" + i));
    }
}
