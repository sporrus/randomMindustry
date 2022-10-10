package randomMindustry.mappers.block.blocks;

import arc.math.Mathf;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.production.GenericCrafter;
import randomMindustry.RMVars;
import randomMindustry.mappers.item.CustomItem;
import randomMindustry.mappers.item.ItemMapper;
import randomMindustry.mappers.item.ItemPack;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomCrafter extends GenericCrafter implements RandomBlock {
    public CustomItem item;

    public RandomCrafter(String name) {
        super(name);
        size = r.random(1, 4);
        health = Mathf.round(r.random(5, 50) * size, 50);

        ItemPack pack = ItemMapper.getLockedPacksByTier("craft").random(r);
        if (pack == null) item = ItemMapper.getPacksByTier("craft").random(r).random(false);
        else item = pack.random(true);
        outputItems = new ItemStack[]{new ItemStack(item, r.random(1, 10))};

        int tier = ItemMapper.getTier(item);
        requirements(Category.crafting, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(25, 250) * size, 5)));
        consumeItems(ItemMapper.getItemStacks(tier - 1, r.random(1, 3), () -> r.random(1, 10)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal crafter name";
        description = "unreal crafter description";
    }
}
