package randomMindustry.mappers.block.blocks;

import arc.math.Mathf;
import mindustry.type.Category;
import mindustry.world.blocks.distribution.ItemBridge;
import randomMindustry.RMVars;
import randomMindustry.mappers.item.ItemMapper;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomItemBridge extends ItemBridge implements RandomBlock {
    public static int lastTier = 1;
    int tier = lastTier++;

    public RandomItemBridge(String name) {
        super(name);
        size = 1;
        health = Mathf.round(r.random(1, 5) * tier, 5);
        fadeIn = moveArrows = false

        requirements(Category.production, ItemMapper.getItemStacks(tier - 1, 2, () -> r.random(6, 10)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        range = 0.03f * (tier * ItemMapper.maxTier / 12f);
        
        if (tier >= 2) {
           fadeIn = moveArrows = true
        }

        localizedName = "unreal item bridge name";
        description = "unreal item bridge description";
    }
}
