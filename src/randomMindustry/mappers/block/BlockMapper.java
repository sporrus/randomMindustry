package randomMindustry.mappers.block;

import arc.math.*;
import arc.struct.*;
import mindustry.*;
import mindustry.world.*;
import randomMindustry.mappers.block.blocks.*;
import randomMindustry.mappers.item.*;
import randomMindustry.random.*;

public class BlockMapper {
    public static final Seq<RandomBlock> generatedBlocks = new Seq<>();
    public static final SyncedRand r = new SyncedRand();
    public static final SyncedRand cr = new SyncedRand();

    public static void generateContent() {
        for (int i = 0; i < ItemMapper.generatedItems.selectTierType(CustomItem.TierType.drill).size; i++)
            generatedBlocks.add(new RandomOre("random-ore-", i));
        for (int i = 0; i < ItemMapper.generatedItems.selectTierType(CustomItem.TierType.drill).size / 3; i++)
            generatedBlocks.add(new RandomDrill("random-drill-", i));
        for (int i = 0; i < ItemMapper.generatedItems.selectTierType(CustomItem.TierType.craft).size; i++)
            generatedBlocks.add(new RandomCrafter("random-crafter-", i));
        for(int i = 0; i < ItemMapper.maxTier / 3; i++)
            generatedBlocks.add(new RandomCore("random-core-", i));
        for(int i = 0; i < ItemMapper.maxTier / 2; i++)
            generatedBlocks.add(new RandomCGenerator("random-cgen-", i));
        
        for (int i = 0; i < ItemMapper.maxTier; i++)
            generatedBlocks.add(new RandomConveyor("random-conveyor-", i));
        for (int i = 0; i < ItemMapper.maxTier; i++)
            generatedBlocks.add(new RandomRouter("random-router-", i));
        for (int i = 0; i < ItemMapper.maxTier; i++)
            generatedBlocks.add(new RandomItemBridge("random-item-bridge-", i));

        for (int i = 0; i < ItemMapper.maxTier; i++)
            generatedBlocks.add(new RandomWall("random-wall-", i));
        for (int i = 0; i < ItemMapper.maxTier; i++)
            generatedBlocks.add(new RandomItemTurret("random-item-turret-", i));

        for(int i = 0; i < ItemMapper.maxTier; i++)
            generatedBlocks.add(new RandomSeaBush("random-plant-", i));
    }

    public static void reloadContent() {
        generatedBlocks.each(RandomBlock::generate);
        if (!Vars.headless) generatedBlocks.each(RandomBlock::reloadIcons);
        generatedBlocks.each(b -> ((Block)b).init());
    }
}
