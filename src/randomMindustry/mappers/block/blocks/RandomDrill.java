package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
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
    public Item mostItem;

    public RandomDrill(String name) {
        super(name);
        size = 2;
        health = Mathf.round(r.random(5, 50) * size, 5);

        this.tier = hardness++;
        drillTime = 600f / this.tier;
        int tier = (this.tier - 1) * 2;
        requirements(Category.production, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(5, 50) * size, 5)));
        mostItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = "unreal drill name";
        description = "unreal drill description";
    }

    @Override
    public float getDrillTime(Item item) {
        if (!(item instanceof CustomItem)) return drillTime;
        return 600f * item.hardness / tier;
    }

    @Override
    public void load() {}

    @Override
    public void loadIcon() {}

    @Override
    public void createIcons(MultiPacker packer) {
        Pixmap sprite = drillSprites.random(packer, 192, 64, r);
        TextureManager.recolorRegion(sprite, mostItem.color);
        Pixmap region = sprite.crop(0, 0, 64, 64);
        Pixmap topRegion = sprite.crop(64, 0, 64, 64);
        Pixmap rotatorRegion = sprite.crop(128, 0, 64, 64);
        Pixmap icon = region.copy();
        icon.draw(topRegion, true);
        icon.draw(rotatorRegion, true);
        this.region = TextureManager.alloc(region);
        this.topRegion = TextureManager.alloc(topRegion);
        this.rotatorRegion = TextureManager.alloc(rotatorRegion);
        this.itemRegion = TextureManager.alloc(packer.get("drill-item-2").crop());
        this.fullIcon = this.uiIcon = TextureManager.alloc(icon);
    }
}
