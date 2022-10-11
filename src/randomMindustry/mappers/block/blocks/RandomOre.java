package randomMindustry.mappers.block.blocks;

import arc.struct.*;
import mindustry.world.blocks.environment.*;
import randomMindustry.mappers.item.*;

public class RandomOre extends OreBlock implements RandomBlock {
    public static final Seq<RandomOre> all = new Seq<>();
    public static int lastTier = 0;

    public RandomOre(String name) {
        super(name, ItemMapper.generatedItems
                .selectTierType(ItemTierType.drill)
                .selectLocalTier((lastTier++) / 3)
                .selectLocked(true)
                .removeNext());
        all.add(this);
    }
}
