package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomConveyor extends Conveyor implements RandomBlock {
    public static int lastTier = 1;
    int tier = lastTier++;

    public RandomConveyor(String name) {
        super(name);
        size = 1;
        health = Mathf.round(r.random(1, 5) * tier, 1);

        requirements(Category.distribution, ItemMapper.getItemStacks(tier - 1, r.random(1, 2), () -> 1));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        speed = 0.03f * tier;
        displayedSpeed = speed * 136;

        localizedName = "unreal conveyor name";
        description = "unreal conveyor description";
    }

    @Override
    public void load() {}

    @Override
    public void loadIcon() {}

    @Override
    public void createIcons(MultiPacker packer) {
        PixmapRegion region = packer.get("random-mindustry-conveyors");
        regions = new TextureRegion[5][4];
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[i].length; j++) {
                Pixmap frame = region.crop(j * 32, i * 32, 32, 32);
                TextureManager.recolorRegion(frame, requirements[0].item.color);
                regions[i][j] = TextureManager.alloc(frame);
            }
        }
        fullIcon = uiIcon = regions[0][0];
    }
}
