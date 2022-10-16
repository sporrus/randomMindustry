package randomMindustry.mappers.block.blocks;

import arc.*;
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
    public final int id;
    public Item mainItem;
    public int tier;

    public RandomConveyor(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
        squareSprite = false;
        if (id == 0) alwaysUnlocked = true;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }

    @Override
    public void reload() {
        generate();
        reloadIcons();
    }

    public void generate() {
        tier = id + 1;

        size = 1;
        health = Mathf.round(r.random(1, 5) * tier, 1);

        requirements(Category.distribution, ItemMapper.getItemStacks(tier - 1, r.random(1, 2), () -> 1));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;

        speed = 0.03f * tier;
        displayedSpeed = speed * 136;

        localizedName = mainItem.localizedName + " Conveyor";
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    private TextureRegion[][] pixmapRegions;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        pixmapRegions = new TextureRegion[5][4];
        for (int i = 0; i < pixmapRegions.length; i++) {
            for (int j = 0; j < pixmapRegions[i].length; j++) {
                Pixmap frame = from.crop(j * 32, i * 32, 32, 32);
                TextureManager.recolorRegion(frame, mainItem.color);
                pixmapRegions[i][j] = TextureManager.alloc(frame);
            }
        }
        pixmapLoaded = true;
    }

    public void applyIcons() {
        regions = pixmapRegions;
        fullIcon = uiIcon = regions[0][0];
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(packer.get("random-mindustry-conveyors").crop());
    }

    public void reloadIcons() {
        TextureRegion region = Core.atlas.find("random-mindustry-conveyors");
        createSprites(region.texture.getTextureData().getPixmap()
                .crop(region.getX(), region.getY(), region.width, region.height));
        applyIcons();
    }
}
