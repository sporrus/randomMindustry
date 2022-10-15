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

import static randomMindustry.RMVars.*;
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

        squareSprite = false;

        localizedName = mainItem.localizedName + " Bridge";
    }

    @Override
    public void load() {
        super.load();
        if (!pixmapLoaded) return;
        region = fullIcon = uiIcon = pixmapRegion;
        endRegion = pixmapEnd;
        bridgeRegion = pixmapBridge;
        arrowRegion = pixmapArrow;
    }

    private boolean pixmapLoaded = false;
    private TextureRegion pixmapRegion;
    private TextureRegion pixmapEnd;
    private TextureRegion pixmapBridge;
    private TextureRegion pixmapArrow;
    @Override
    public void createIcons(MultiPacker packer) {
        Pixmap pixmap = bridgeSprites.random(packer, 128, 32, r);
        TextureManager.recolorRegion(pixmap, mainItem.color);
        pixmapRegion = TextureManager.alloc(pixmap.crop(0, 0, 32, 32));
        pixmapEnd = TextureManager.alloc(pixmap.crop(32, 0, 32, 32));
        pixmapBridge = TextureManager.alloc(pixmap.crop(64, 0, 32, 32));
        pixmapArrow = TextureManager.alloc(pixmap.crop(96, 0, 32, 32));
        pixmapLoaded = true;
    }
}
