package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.mappers.item.ItemMapper.*;

public class RandomCrafter extends GenericCrafter implements RandomBlock {
    public static int lastTier = 0;
    public CustomItem item;

    public RandomCrafter(String name) {
        super(name);
        size = 2;
        health = Mathf.round(r.random(5, 50) * size, 5);

        int tier = lastTier++ / 3;
        CustomItemSeq items = ItemMapper.generatedItems
                .selectTierType(ItemTierType.craft)
                .selectLocalTier(tier)
                .selectLocked(true);
        item = items.lockNext(false);
        outputItems = new ItemStack[]{new ItemStack(item, r.random(1, 10))};

        requirements(Category.crafting, ItemMapper.getItemStacks(tier * 2, r.random(1, 5), () -> Mathf.round(r.random(10, 100) * size, 5)));
        consumeItems(ItemMapper.getItemStacks(tier * 2, r.random(1, 3), () -> r.random(1, 10)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal crafter name";
        description = "unreal crafter description";
    }

    @Override
    public void load() {
        super.load();
        region = fullIcon;
    }

    @Override
    public void loadIcon() {}

    @Override
    public void createIcons(MultiPacker packer) {
        PixmapRegion region = packer.get("random-mindustry-crafters");
        int sprite = r.random(0, RMVars.crafterSprites - 1);
        int x = (sprite % RMVars.crafterSpriteX) * 64;
        int y = (sprite / RMVars.crafterSpriteX) * 64;
        Pixmap pixmap = region.crop(x, y, 64, 64);
        TextureManager.recolorRegion(pixmap, item.color);
        this.region = fullIcon = uiIcon = TextureManager.alloc(pixmap);
    }
}
