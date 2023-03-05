package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.audio.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import mindustry.world.meta.*;
import randomMindustry.*;
import randomMindustry.mappers.bullet.*;
import randomMindustry.mappers.item.*;
import randomMindustry.mappers.sound.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomItemTurret extends ItemTurret implements RandomBlock {
    public static final Seq<RandomItemTurret> last = new Seq<>();
    public final int id;
    public Item mainItem;

    public RandomItemTurret(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    public void generate() {
        if (id == 0) {
            alwaysUnlocked = true;
            last.clear();
        }
        stats = new Stats();
        // cancer
        Consume[] consumes = consumers.clone();
        consumers = new Consume[0];
        for (Consume consume : consumes) removeConsumer(consume);
        ammoTypes.clear();

        size = (int) Math.max(1, Math.min(4, getTier() / 3f));
        requirements(Category.turret, ItemMapper.getItemStacks(getTier(), r.random(1, 5), () -> Mathf.round(r.random(10, 50) * size, 5), r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;

        health = Mathf.round(r.random(125, 175) * size * getTier(), 5);
        reload = r.random(1f, 30f);
        range = r.random(112f, Math.max(800f * getTier() / 20f, 160f));
        rotateSpeed = r.random(0.5f, 5f);
        inaccuracy = r.random(1f, 90f / getTier() * 10f / reload);
        shootSound = SoundMapper.randomShoot();
        int itemCount = r.random(1, 3);
        CustomItemSeq seq = ItemMapper.generatedItems.selectGlobalTierRange(getTier() - 1, getTier());
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = seq.random(r);
            seq.remove(item);
            float damage = (float)Math.round(getTier() * reload + r.random(-getTier(), getTier()) / 4f);
            float speed = r.random(1f, 10f);
            BulletType bullet = BulletMapper.random(getTier(), this);
            bullet.damage = damage;
            bullet.speed = speed;
            if ((bullet.healAmount > 0 || bullet.healPercent > 0) && targetHealing == false) targetHealing = true;
            bullet.trailColor = item.color;
            bullet.hitColor = item.color.cpy().mul(1.5f);

            if (bullet instanceof BasicBulletType ba) {
                ba.backColor = item.color;
                ba.frontColor = item.color.cpy().mul(1.5f);
            }
            
            ammoTypes.put(item, bullet);
        }
        limitRange();

        localizedName = mainItem.localizedName + " " + Seq.with("Turret", "Tower", "Gun", "Catapult").random();
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

    @Override
    public void loadIcon() {}

    @Override
    public int getTier() {
        return id + 1;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(tierStat, t -> t.add(Integer.toString(getTier())));
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
}
