package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomDrill extends Drill implements RandomBlock {
    public static int hardness = 1;
    public Item mainItem;

    public RandomDrill(String name) {
        super(name);
        size = 2;
        health = Mathf.round(r.random(5, 50) * size, 5);

        this.tier = hardness++;
        drillTime = 600f / this.tier;
        int tier = (this.tier - 1) * 2;
        requirements(Category.production, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(5, 50) * size, 5)));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = mainItem + " Drill";
    }

    @Override
    public float getDrillTime(Item item) {
        if (!(item instanceof CustomItem)) return drillTime;
        return 600f * item.hardness / tier;
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (!pixmapLoaded) return;
        region = pixmapRegion;
        topRegion = pixmapTopRegion;
        rotatorRegion = pixmapRotatorRegion;
        itemRegion = pixmapItemRegion;
        fullIcon = uiIcon = pixmapIcon;
    }

    private TextureRegion pixmapRegion;
    private TextureRegion pixmapTopRegion;
    private TextureRegion pixmapRotatorRegion;
    private TextureRegion pixmapItemRegion;
    private TextureRegion pixmapIcon;
    private boolean pixmapLoaded = false;
    @Override
    public void createIcons(MultiPacker packer) {
        Pixmap sprite = drillSprites.random(packer, 192, 64, r);
        TextureManager.recolorRegion(sprite, mainItem.color);
        Pixmap region = sprite.crop(0, 0, 64, 64);
        Pixmap topRegion = sprite.crop(64, 0, 64, 64);
        Pixmap rotatorRegion = sprite.crop(128, 0, 64, 64);
        Pixmap icon = region.copy();
        icon.draw(topRegion, true);
        icon.draw(rotatorRegion, true);
        pixmapRegion = TextureManager.alloc(region);
        pixmapTopRegion = TextureManager.alloc(topRegion);
        pixmapRotatorRegion = TextureManager.alloc(rotatorRegion);
        pixmapItemRegion = TextureManager.alloc(packer.get("drill-item-2").crop());
        pixmapIcon = TextureManager.alloc(icon);
        pixmapLoaded = true;
    }
}
