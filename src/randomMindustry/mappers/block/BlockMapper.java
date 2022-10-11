package randomMindustry.mappers.block;

import arc.struct.*;
import randomMindustry.*;
import randomMindustry.mappers.*;
import randomMindustry.mappers.block.blocks.*;
import randomMindustry.mappers.item.*;

public class BlockMapper implements Mapper {
    public static final Seq<RandomBlock> generatedBlocks = new Seq<>();
    public static final SyncedRand r = new SyncedRand();

    @Override
    public void generateContent() {
        for (int i = 0; i < ItemMapper.generatedItems.selectTierType(ItemTierType.drill).size; i++)
            generatedBlocks.add(new RandomOre("random-ore-" + i));
        for (int i = 0; i < ItemMapper.maxTier / 2; i++)
            generatedBlocks.add(new RandomDrill("random-drill-" + i));
        for (int i = 0; i < ItemMapper.generatedItems.selectTierType(ItemTierType.craft).size; i++)
            generatedBlocks.add(new RandomCrafter("random-crafter-" + i));
        for (int i = 0; i < 12; i++)
            generatedBlocks.add(new RandomWall("random-wall-" + i));
        for (int i = 0; i < 6; i++)
            generatedBlocks.add(new RandomConveyor("random-conveyor-" + i));
    }
}
