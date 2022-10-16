package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomWall extends Wall implements RandomBlock {
    public final int id;
    public Item mainItem;
    public int tier;

    public RandomWall(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
        squareSprite = false;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }

    public void reload() {
        generate();
        reloadIcons();
    }

    public void generate() {
        tier = id + 1;

        size = r.random(1, 2);
        health = Mathf.round(r.random(100, 500) * size * tier, 5);

        requirements(Category.defense, ItemMapper.getItemStacks(tier - 1, r.random(1, 3), () -> 6 * size * size));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;

        localizedName = mainItem.localizedName + " Wall";
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
    }

    private TextureRegion pixmapRegion;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, mainItem.color);
        pixmapRegion = TextureManager.alloc(from);
        pixmapLoaded = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(wallSprites.get(size).random(packer, size * 32, r));
    }

    public void reloadIcons() {
        createSprites(wallSprites.get(size).random(size * 32, r));
        applyIcons();
    }
}
