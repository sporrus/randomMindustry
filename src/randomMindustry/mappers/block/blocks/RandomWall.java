package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomWall extends Wall implements RandomBlock {
    public static int lastTier = 1;
    public Item mainItem;
    public int tier = lastTier++;

    public RandomWall(String name) {
        super(name);
        size = r.random(1, 2);
        health = Mathf.round(r.random(100, 500) * size * tier, 5);

        requirements(Category.defense, ItemMapper.getItemStacks(tier - 1, r.random(1, 3), () -> Mathf.round(r.random(5, 50) * size, 5)));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        localizedName = mainItem.localizedName + " Wall";
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
        super.createIcons(packer);
        Pixmap pixmap = wallSprites.get(size).random(packer, size * 32, r);
        TextureManager.recolorRegion(pixmap, mainItem.color);
        pixmapRegion = TextureManager.alloc(pixmap);
        pixmapLoaded = true;
    }
}
