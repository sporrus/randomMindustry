package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;

import randomMindustry.random.util.*;
import randomMindustry.util.*;

import static mindustry.Vars.*;

public class BulletMapper {
    public static void init() {
        Seq<Effect> effects = FxMapper.effects.copy();
        Seq<BulletType> bullets = content.bullets().copy();
        RandomUtil.shuffle(bullets);
        bullets.each(bullet -> {
            Seq<BulletType> bulletSeq = content.bullets().select(b -> b != bullet);

            bullet.hitEffect = effects.random(RandomUtil.getClientRand());
            bullet.despawnEffect = effects.random(RandomUtil.getClientRand());
            bullet.shootEffect = effects.random(RandomUtil.getClientRand());
            bullet.chargeEffect = effects.random(RandomUtil.getClientRand());
            bullet.smokeEffect = effects.random(RandomUtil.getClientRand());
            bullet.trailEffect = effects.random(RandomUtil.getClientRand());
            bullet.healEffect = effects.random(RandomUtil.getClientRand());

            bullet.hitSound = Util.generateSound();
            bullet.despawnSound = Util.generateSound();

            bullet.weaveScale = RandomUtil.getRand().random(0.5f, 20f);
            bullet.weaveMag = RandomUtil.getRand().random(0.5f, 10f);

            bullet.rangeChange = RandomUtil.getRand().random(0.5f, 80f);
            bullet.scaleLife = RandomUtil.getRand().random(-2f, 2f) < 0f;
            bullet.healPercent = RandomUtil.getRand().chance(0.75f) ? RandomUtil.getRand().random(0f, 100f) : 0;
            if (bullet.healPercent > 0) bullet.collidesTiles = bullet.collidesTeam = true;
            else bullet.collidesTeam = false;
            bullet.lifetime = RandomUtil.getRand().random(10f, 100f);
            bullet.recoil = RandomUtil.getRand().random(-1f, 1f);
            bullet.knockback = RandomUtil.getRand().random(-10f, 10f);
            bullet.hitShake = RandomUtil.getClientRand().random(-1f, 1f);
            bullet.despawnShake = RandomUtil.getClientRand().random(-1f, 1f);

            bullet.speed = RandomUtil.getRand().random(1f, 10f);
            bullet.drag = RandomUtil.getRand().random(-0.01f, 0.08f);

            bullet.damage = RandomUtil.getRand().random(1f, 100f);
            bullet.buildingDamageMultiplier = RandomUtil.getRand().random(0f, 2f);

            if (bullet instanceof BasicBulletType basicBullet) {
                basicBullet.width = RandomUtil.getRand().random(4f, 20f);
                basicBullet.height = RandomUtil.getRand().random(4f, 20f);
                basicBullet.shrinkX = RandomUtil.getRand().random(-1f, 1f);
                basicBullet.shrinkY = RandomUtil.getRand().random(-1f, 1f);
                basicBullet.spin = RandomUtil.getRand().random(45f);
            }

            if (RandomUtil.getRand().chance(0.5f)) {
                bullet.status = content.statusEffects().random(RandomUtil.getRand());
                bullet.statusDuration = RandomUtil.getRand().random(600f);
            }

            if (RandomUtil.getRand().chance(0.5f)) {
                bullet.fragBullets = RandomUtil.getRand().random(10);
                bullet.fragBullet = RandomUtil.getRand().random(-10f, 2f) < 0f ? null : bulletSeq.random(RandomUtil.getRand());
            }

            if (RandomUtil.getRand().chance(0.25f)) {
                bullet.intervalBullets = RandomUtil.getRand().random(5);
                bullet.intervalBullet = RandomUtil.getRand().random(-10f, 2f) < 0f ? null : bulletSeq.random(RandomUtil.getRand());
                bullet.bulletInterval = RandomUtil.getRand().random(60f, 600f);
            }

            bullet.init();
        });
    }
}
