package randomMindustry.mappers.block.blocks;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import randomMindustry.RMVars;
import randomMindustry.mappers.block.BlockMapper;
import randomMindustry.mappers.item.CustomItem;
import randomMindustry.mappers.item.ItemMapper;
import randomMindustry.mappers.item.ItemPack;
import randomMindustry.texture.TextureManager;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomCrafter extends GenericCrafter implements RandomBlock {
    public RandomCrafter(String name) {
        super(name);
    }

    @Override
    public void edit() {
        CustomItem item;
        ItemPack pack = ItemMapper.getLockedPacksByTier("craft").random(r);
        if (pack == null) item = ItemMapper.getPacksByTier("craft").random(r).random(false);
        else item = pack.random(true);
        this.outputItems = new ItemStack[]{new ItemStack(item, r.random(1, 10))};
        int tier = ItemMapper.getTier(item);
        this.consumeItems(ItemMapper.getItemStacks(tier - 1, r.random(1, 3), () -> r.random(1, 10)));
        this.stats.add(RMVars.seedStat, RMVars.seedStatValue);

        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof GenericCrafter && !BlockMapper.generated(b)).random(r);
        this.region = TextureManager.alloc(copyBlock.region);
        this.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        this.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        this.size = this.region.width / 32;
        this.localizedName = "unreal crafter name";
        this.description = "unreal crafter description";

        this.requirements = ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(25, 1000), 5));
    }
}
