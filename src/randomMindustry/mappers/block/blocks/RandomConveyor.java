package randomMindustry.mappers.block.blocks;

import arc.math.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomConveyor extends Conveyor implements RandomBlock {
    public static int lastTier = 1;
    int tier = lastTier++;

    public RandomConveyor(String name) {
        super(name);
        size = 1;
        health = Mathf.round(r.random(1, 5) * tier, 1);

        requirements(Category.distribution, ItemMapper.getItemStacks(tier - 1, r.random(1, 2), () -> r.random(1, 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        speed = 0.03f * tier;
        displayedSpeed = speed * 136;

        localizedName = "unreal conveyor name";
        description = "unreal conveyor description";
    }
}
