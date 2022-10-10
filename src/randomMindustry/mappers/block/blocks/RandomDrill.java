package randomMindustry.mappers.block.blocks;

import arc.math.Mathf;
import arc.util.Log;
import mindustry.type.Category;
import mindustry.world.blocks.production.Drill;
import randomMindustry.RMVars;
import randomMindustry.mappers.item.ItemMapper;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomDrill extends Drill implements RandomBlock {
    public static int hardness = 1;

    public RandomDrill(String name) {
        super(name);
        size = 2;
        health = Mathf.round(r.random(5, 50) * size, 50);

        this.tier = hardness++;
        int tier = this.tier * 2;
        Log.info(tier);
        requirements(Category.production, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(25, 250) * size, 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal drill name";
        description = "unreal drill description";
    }
}
