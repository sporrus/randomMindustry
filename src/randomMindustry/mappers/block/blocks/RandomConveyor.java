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

public class RandomConveyor extends Conveyor implements RandomBlock {
    public static int lastTier = 1;
    public Item mainItem;
    public int tier = lastTier++;

    public RandomConveyor(String name) {
        super(name);
        size = 1;
        health = Mathf.round(r.random(1, 5) * tier, 1);

        requirements(Category.distribution, ItemMapper.getItemStacks(tier - 1, r.random(1, 2), () -> 1));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        speed = 0.03f * tier;
        displayedSpeed = speed * 136;

        localizedName = mainItem.localizedName + " conveyor";
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        regions = pixmapRegions;
        if (regions != null) fullIcon = uiIcon = regions[0][0];
    }

    private TextureRegion[][] pixmapRegions;
    @Override
    public void createIcons(MultiPacker packer) {
        PixmapRegion region = packer.get("random-mindustry-conveyors");
        pixmapRegions = new TextureRegion[5][4];
        for (int i = 0; i < pixmapRegions.length; i++) {
            for (int j = 0; j < pixmapRegions[i].length; j++) {
                Pixmap frame = region.crop(j * 32, i * 32, 32, 32);
                TextureManager.recolorRegion(frame, mainItem.color);
                pixmapRegions[i][j] = TextureManager.alloc(frame);
            }
        }
    }
}
