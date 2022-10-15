package randomMindustry.mappers.block.blocks;

import arc.math.*;
import mindustry.entities.bullet.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomItemTurret extends ItemTurret implements RandomBlock {
    public static int lastTier = 0;
    int tier = ++lastTier;

    public RandomItemTurret(String name) {
        super(name);
        size = (int) Math.max(1, Math.min(4, tier / 3f));
        health = Mathf.round(r.random(5, 50) * size * tier, 5);

        reload = r.random(0.5f, 50f);
        range = r.random(64f, 800f * tier / 20f);
        rotateSpeed = r.random(0.5f, 5f);
        inaccuracy = r.random(1f, 90f / tier * 10f / reload);

        int itemCount = r.random(1, 3);
        CustomItemSeq seq = ItemMapper.generatedItems.selectGlobalTierRange(tier - 2, tier - 1);
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = seq.random(r);
            seq.remove(item);
            float damage = r.random(1f, 10f) * tier * reload / 10;
            float speed = r.random(1f, 10f);
            ammoTypes.put(item, new BasicBulletType(speed, damage){{
                width = 7f;
                height = 9f;
                lifetime = range / speed;
            }});
        }

        requirements(Category.turret, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(10, 100) * size, 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);

        limitRange();
        
        localizedName = "Tristan the Turret"
    }
}
