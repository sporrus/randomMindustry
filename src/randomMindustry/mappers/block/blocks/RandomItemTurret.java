package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
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
    public static int lastTier = 0;
    public Item mainItem;
    public int tier = ++lastTier;

    public RandomItemTurret(String name) {
        super(name);
        size = (int) Math.max(1, Math.min(4, tier / 3f));
        health = Mathf.round(r.random(100, 200) * size * tier, 5);

        reload = r.random(1f, 30f);
        range = r.random(64f, 800f * tier / 20f);
        rotateSpeed = r.random(0.5f, 5f);
        inaccuracy = r.random(1f, 90f / tier * 10f / reload);

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
                if(tier > 1) homingPower = r.random(0.025f, 0.2f);
            }});
        }

        requirements(Category.turret, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(10, 100) * size, 5)));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        limitRange();
        
        localizedName = mainItem.localizedName + " " + Seq.with("Turret", "Tower", "Gun", "Catapult").random();
    }

    @Override
    public void load() {
        super.load();
        if (!pixmapLoaded) return;
        region = ((DrawTurret) drawer).preview = pixmapTurret;
        fullIcon = uiIcon = pixmapIcon;
    }

    private boolean pixmapLoaded = false;
    private TextureRegion pixmapTurret;
    private TextureRegion pixmapIcon;
    @Override
    public void createIcons(MultiPacker packer) {
        Pixmap pixmap = itemTurretSprites.get(size).random(packer, size * 32, r);
        TextureManager.recolorRegion(pixmap, mainItem.color);
        pixmap = Pixmaps.outline(new PixmapRegion(pixmap), outlineColor, outlineRadius);
        Pixmap icon = packer.get("block-" + size).crop();
        icon.draw(pixmap, true);
        pixmapTurret = TextureManager.alloc(pixmap);
        pixmapIcon = TextureManager.alloc(icon);
        pixmapLoaded = true;
    }
}
