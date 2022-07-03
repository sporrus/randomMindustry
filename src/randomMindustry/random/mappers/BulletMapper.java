package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.gen.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;

import randomMindustry.random.util.*;
import randomMindustry.util.Util;

import static mindustry.Vars.*;

public class BulletMapper {
    public static void init() {
        Seq<Effect> effects = Effect.all.select(effect -> effect != Fx.dynamicExplosion);

        Seq<BulletType> bullets = content.bullets().copy();
        RandomUtil.shuffle(bullets);
        bullets.each(bullet -> {
            Seq<BulletType> bulletSeq = content.bullets().select(b -> b != bullet);
            
            bullet.hitSound = Util.generateSound();
            bullet.despawnSound = Util.generateSound();

            bullet.weaveScale = RandomUtil.getRand().random(0.5f, 20f);
            bullet.weaveMag = RandomUtil.getRand().random(0.5f, 10f);
            
            bullet.rangeChange = RandomUtil.getRand().random(0.5f, 20f);
            bullet.scaleLife = RandomUtil.getRand().random(-2f, 2f) < 0f;
            bullet.healPercent = (RandomUtil.getRand().chance(0.25) ? RandomUtil.getRand().random(0f, 100f) : 0);
            if (bullet.healPercent > 0) bullet.collidesTiles = bullet.collidesTeam = true;
            else bullet.collidesTeam = false;
            bullet.lifetime = RandomUtil.getRand().random(10f, 100f);
            bullet.recoil = RandomUtil.getRand().random(-1f, 1f);
            bullet.knockback = RandomUtil.getRand().random(-10f, 10f);
            bullet.hitShake = RandomUtil.getRand().random(-1f, 1f);
            bullet.despawnShake = RandomUtil.getRand().random(-1f, 1f);
            
            bullet.speed = RandomUtil.getRand().random(1f, bullet.speed + (bullet.speed / 2f));
            bullet.drag = RandomUtil.getRand().random(-0.01f, 0.05f);

            bullet.damage = RandomUtil.getRand().random(1f, 100f);
            bullet.buildingDamageMultiplier = RandomUtil.getRand().random(0f, 2f);

            if (bullet instanceof BasicBulletType basicBullet) {
                basicBullet.width = RandomUtil.getRand().random(4f, 20f);
                basicBullet.height = RandomUtil.getRand().random(4f, 20f);
                basicBullet.shrinkX = RandomUtil.getRand().random(-1f, 1f);
                basicBullet.shrinkY = RandomUtil.getRand().random(-1f, 1f);
                basicBullet.spin = RandomUtil.getRand().random(45f);
                basicBullet.shrinkX = RandomUtil.getRand().random(-1f, 1f);
                basicBullet.shrinkY = RandomUtil.getRand().random(-1f, 1f);
                basicBullet.spin = RandomUtil.getRand().random(45f);
            }
 
            bullet.status = content.statusEffects().random(RandomUtil.getRand());
            bullet.statusDuration = RandomUtil.getRand().random(600f);
            
            bullet.fragBullets = RandomUtil.getRand().random(1, 15);
            bullet.fragBullet = RandomUtil.getRand().random(-2f, 2f) < 0f ? null : bulletSeq.random();
            
            bullet.intervalBullets = RandomUtil.getRand().random(2, 6);
            bullet.intervalBullet = RandomUtil.getRand().random(-2f, 2f) < 0f ? null : bulletSeq.random();
            bullet.bulletInterval = RandomUtil.getRand().random(60f, 600f);
        });
    }
}
