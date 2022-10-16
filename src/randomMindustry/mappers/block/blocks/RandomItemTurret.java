package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.bullet.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.draw.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomItemTurret extends ItemTurret implements RandomBlock {
    public final int id;
    public Item mainItem;
    public int tier;

    public RandomItemTurret(String name, int id) {
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

    @Override
    public void reload() {
        generate();
        reloadIcons();
    }

    public void generate() {
        tier = id + 1;

        size = (int) Math.max(1, Math.min(4, tier / 3f));
        health = Mathf.round(r.random(100, 200) * size * tier, 5);

        reload = r.random(1f, 30f);
        range = r.random(64f, 800f * tier / 20f);
        rotateSpeed = r.random(0.5f, 5f);
        inaccuracy = r.random(1f, 90f / tier * 10f / reload);

        ammoTypes.clear();
        int itemCount = r.random(1, 3);
        CustomItemSeq seq = ItemMapper.generatedItems.selectGlobalTierRange(tier - 2, tier - 1);
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = seq.random(r);
            seq.remove(item);
            float damage = r.random(1f, 10f) + tier * reload;
            float speed = r.random(1f, 10f);
            ammoTypes.put(item, new BasicBulletType(speed, damage){{
                width = r.random(5f, 10f);
                height = r.random(width, width + 10);
                lifetime = range / speed;
                if(tier > 2) homingPower = r.random(0.025f, 0.2f);
                backColor = item.color;
                frontColor = hitColor = item.color.cpy().mul(1.5f);
            }});
        }

        requirements(Category.turret, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(10, 100) * size, 5)));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        limitRange();
        localizedName = mainItem.localizedName + " " + Seq.with("Turret", "Tower", "Gun", "Catapult").random();
    }

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    // TODO: this is bad
    public void applyIcons() {
        region = ((DrawTurret) drawer).preview = pixmapTurret;
        TextureRegion baseRegion = ((DrawTurret) drawer).base;
        Pixmap base = baseRegion.texture.getTextureData().getPixmap()
                .crop(baseRegion.getX(), baseRegion.getY(), baseRegion.width, baseRegion.height);
        base.draw(region.texture.getTextureData().getPixmap()
                .crop(region.getX(), region.getY(), region.width, region.height), true);
        fullIcon = uiIcon = TextureManager.alloc(base);

        ammoTypes.forEach(e -> e.value.load());
    }

    private boolean pixmapLoaded = false;
    private TextureRegion pixmapTurret;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, mainItem.color);
        from = Pixmaps.outline(new PixmapRegion(from), outlineColor, outlineRadius);
        pixmapTurret = TextureManager.alloc(from);
        pixmapLoaded = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(itemTurretSprites.get(size).random(packer, size * 32, r));
    }

    public void reloadIcons() {
        createSprites(itemTurretSprites.get(size).random(size * 32, r));
        applyIcons();
    }
}
