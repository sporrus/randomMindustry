package randomMindustry.mappers.block.blocks;

import arc.math.Mathf;
import mindustry.type.Category;
import mindustry.world.blocks.defense.Wall;
import randomMindustry.RMVars;
import randomMindustry.mappers.item.ItemMapper;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomWall extends Wall implements RandomBlock {
    public RandomWall(String name) {
        super(name);
        size = r.random(1, 4);
        health = Mathf.round(r.random(500, 1000) * size, 50);

        requirements(Category.defense, ItemMapper.getItemStacks(r.random(0, ItemMapper.maxTier), r.random(1, 5), () -> Mathf.round(r.random(25, 1000), 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal wall name";
        description = "unreal wall description";
    }
}
