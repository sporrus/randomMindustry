package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomItemTurret extends ItemTurret implements RandomBlock {
    public static final Seq<RandomItemTurret> last = new Seq<>();
    public final int id;
    public Item mainItem;
    public int tier;

    public RandomItemTurret(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    @Override
    public TechTree.TechNode generateNode() {
        techNode = new TechTree.TechNode(
                last.size == 0 ? TechTree.context() : last.random(r).techNode,
                this,
                researchRequirements()
        );
        if (r.chance(0.5) && last.size > 0) last.remove(0);
        last.add(this);
        return techNode;
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

    public void generate() {
        if (id == 0) {
            alwaysUnlocked = true;
            last.clear();
        }
        tier = id + 1;

        size = (int) Math.max(1, Math.min(4, tier / 3f));
        health = Mathf.round(r.random(100, 200) * size * tier, 5);

        reload = r.random(1f, 30f);
        range = r.random(112f, Math.max(800f * tier / 20f, 160f));
        rotateSpeed = r.random(0.5f, 5f);
        inaccuracy = r.random(1f, 90f / tier * 10f / reload);

        // cancer
        Consume[] consumes = consumers.clone();
        consumers = new Consume[0];
        for (Consume consume : consumes) removeConsumer(consume);

        ammoTypes.clear();
        int itemCount = r.random(1, 3);
        CustomItemSeq seq = ItemMapper.generatedItems.selectGlobalTierRange(tier - 2, tier - 1);
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = seq.random(r);
            seq.remove(item);
            float damage = (float)Math.round(tier * reload + r.random(-tier, tier) / 4f);
            float speed = r.random(1f, 10f);
            ammoTypes.put(item, new BasicBulletType(speed, damage){{
                width = r.random(10f, 15f);
                height = r.random(width, width + 10);
                if(r.chance(0.35)) homingPower = r.random(0.025f, 0.2f);
                if(r.chance(0.35)) {
                    splashDamage = damage * r.random(0.5f, 2f);
                    splashDamageRadius = r.random(8f, 40f);
                }
                if(r.chance(0.35)) {
                    lightning = r.random(1, 3);
                    lightningLength = r.random(2, 7);
                    lightningDamage = damage * r.random(0.25f, 0.5f);
                    lightningColor = item.color;
                }
                if (r.chance(0.35)) {
                    healAmount = damage * r.random(0.1f, 0.9f);
                    healColor = item.color;
                    targetHealing = collidesTeam = true;
                }
                backColor = item.color;
                frontColor = hitColor = item.color.cpy().mul(1.5f);
            }});
        }
        limitRange();

        requirements(Category.turret, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(10, 50) * size, 5), r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
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

        ammoTypes.each((k, v) -> v.load());
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
        createSprites(itemTurretSprites.get(size).random(packer, size * 32, cr));
    }

    public void reloadIcons() {
        createSprites(itemTurretSprites.get(size).random(size * 32, cr));
        applyIcons();
    }
}
