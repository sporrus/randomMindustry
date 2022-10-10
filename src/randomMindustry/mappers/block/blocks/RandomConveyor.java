package randomMindustry.mappers.block.blocks;

import arc.math.Mathf;
import mindustry.type.Category;
import mindustry.world.blocks.distribution.Conveyor;
import randomMindustry.RMVars;
import randomMindustry.mappers.item.ItemMapper;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomConveyor extends Conveyor implements RandomBlock {
    public static int lastTier = 1;
    int tier = lastTier++;

    public RandomConveyor(String name) {
        super(name);
        size = 1;
        health = Mathf.round(r.random(1, 5) * tier, 5);

        requirements(Category.production, ItemMapper.getItemStacks(tier - 1, r.random(1, 2), () -> r.random(1, 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        speed = 0.03f * (tier * ItemMapper.maxTier / 12f);
        displayedSpeed = speed * 136;

        localizedName = "unreal conveyor name";
        description = "unreal conveyor description";
    }
}
