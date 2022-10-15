package randomMindustry.mappers.block.blocks;

import arc.math.*;
import arc.struct.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomItemBridge extends ItemBridge implements RandomBlock {
    public static int lastTier = 1;
    public Item mainItem;
    public int tier = lastTier++;

    public RandomItemBridge(String name) {
        super(name);
        size = 1;
        health = Mathf.round(r.random(1, 10) * tier, 1);

        requirements(Category.distribution, ItemMapper.getItemStacks(tier - 1, r.random(1, 2), () -> r.random(1, 10)));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        itemCapacity = 10 * tier;
        range = tier + 2;

        localizedName = mainItem.localizedName + " Bridge";
    }


}
