package randomMindustry.mappers.block.blocks;

import arc.math.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomWall extends Wall implements RandomBlock {
    public RandomWall(String name) {
        super(name);
        size = r.random(1, 4);
        health = Mathf.round(r.random(500, 1000) * size, 5);

        requirements(Category.defense, ItemMapper.getItemStacks(r.random(0, ItemMapper.maxTier), r.random(1, 5), () -> Mathf.round(r.random(5, 50) * size, 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal wall name";
        description = "unreal wall description";
    }
}
0