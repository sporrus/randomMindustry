package randomMindustry.mappers.block.blocks;

import arc.math.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomCrafter extends GenericCrafter implements RandomBlock {
    public static int lastTier = 0;
    public CustomItem item;

    public RandomCrafter(String name) {
        super(name);
        size = r.random(1, 4);
        health = Mathf.round(r.random(5, 50) * size, 5);

        int tier = (lastTier++ / 3);
        CustomItemSeq items = ItemMapper.generatedItems
                .selectTierType(ItemTierType.craft)
                .selectLocalTier(tier)
                .selectLocked(true);
        item = items.removeNext();
        outputItems = new ItemStack[]{new ItemStack(item, r.random(1, 10))};

        requirements(Category.crafting, ItemMapper.getItemStacks(tier * 2, r.random(1, 5), () -> Mathf.round(r.random(10, 100) * size, 5)));
        consumeItems(ItemMapper.getItemStacks(tier * 2, r.random(1, 3), () -> r.random(1, 10)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal crafter name";
        description = "unreal crafter description";
    }
}
