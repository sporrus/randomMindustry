package randomMindustry.mappers.block.blocks;

import arc.math.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomDrill extends Drill implements RandomBlock {
    public static int hardness = 1;

    public RandomDrill(String name) {
        super(name);
        size = 2;
        health = Mathf.round(r.random(5, 50) * size, 5);

        this.tier = hardness++;
        drillTime = 600f / this.tier;
        int tier = (this.tier - 1) * 2;
        requirements(Category.production, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(5, 50) * size, 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal drill name";
        description = "unreal drill description";
    }

    @Override
    public float getDrillTime(Item item) {
        if (!(item instanceof CustomItem)) return drillTime;
        return 600f * item.hardness / tier;
    }
}
