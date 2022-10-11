package randomMindustry.mappers.block.blocks;

import arc.math.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomWall extends Wall implements RandomBlock {
    public static int lastTier = 1;
    int tier = lastTier++;

    public RandomWall(String name) {
        super(name);
        size = r.random(1, 4);
        health = Mathf.round(r.random(100, 500) * size * tier, 5);

        requirements(Category.defense, ItemMapper.getItemStacks(tier - 1, r.random(1, 3), () -> Mathf.round(r.random(5, 50) * size, 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal wall name";
        description = "unreal wall description";
    }
}