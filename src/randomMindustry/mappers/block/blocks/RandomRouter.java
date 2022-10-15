package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.mappers.block.BlockMapper.*;
import static randomMindustry.RMVars.*;

public class RandomRouter extends Router implements RandomBlock {
    public static int lastTier = 1;
    public Item mainItem;
    public int tier = lastTier++;

    public RandomRouter(String name) {
        super(name);
        size = 1;
        health = Mathf.round(r.random(3, 8) * tier, 1);

        requirements(Category.distribution, ItemMapper.getItemStacks(tier - 1, r.random(1, 3), () -> 1));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        speed = 5.5f * tier;
        squareSprite = false;

        localizedName = mainItem.localizedName + " Router";
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (!pixmapLoaded) return;
        region = fullIcon = uiIcon = pixmapRegion;
    }

    private TextureRegion pixmapRegion;
    private boolean pixmapLoaded = false;
    @Override
    public void createIcons(MultiPacker packer) {
        Pixmap pixmap = routerSprites.random(packer, 32, 32, r);
        TextureManager.recolorRegion(pixmap, mainItem.color);
        pixmapRegion = TextureManager.alloc(pixmap);
        pixmapLoaded = true;
    }
}
