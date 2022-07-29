package randomMindustry.random.mappers;

import arc.math.Mathf;
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
            Seq<BulletType> bulletSeq = content.bullets().select(b -> {
                int fragDepth = 0;
                boolean inFrag = false;
                BulletType current = b;
                Seq<BulletType> went = new Seq<>();
                while (current.fragBullet != null) {
                    if (current.fragBullet == b || went.contains(current.fragBullet) || fragDepth >= 2) {
                        inFrag = true;
                        break;
                    }
                    went.add(current);
                    current = current.fragBullet;
                    fragDepth++;
                }
                return b != bullet && !inFrag && !(b instanceof MassDriverBolt);
            });

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

            bullet.homingPower = RandomUtil.getRand().random(0f, 10f);
            bullet.homingRange = RandomUtil.getRand().random(60f, 180f);

            bullet.collidesGround = bullet.collidesAir = false;
            if (RandomUtil.getRand().chance(0.5)) {
                bullet.collidesAir = true;
                bullet.collidesGround = true;
            } else if (RandomUtil.getRand().chance(0.5)) {
                bullet.collidesAir = true;
            } else {
                bullet.collidesGround = true;
            }

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

            bullet.fragBullet = null;
            if (RandomUtil.getRand().chance(0.25f)) {
                bullet.fragBullets = RandomUtil.getRand().random(1, 10);
                bullet.fragBullet = bulletSeq.random(RandomUtil.getRand());
            }

            bullet.intervalBullet = null;
            if (RandomUtil.getRand().chance(0.25f)) {
                bullet.intervalBullets = RandomUtil.getRand().random(1, 5);
                bullet.intervalBullet = bulletSeq.random(RandomUtil.getRand());
                bullet.bulletInterval = RandomUtil.getRand().random(60f, 600f);
            }

            bullet.puddles = 0;
            if (RandomUtil.getRand().chance(0.25f)) {
                bullet.puddles = RandomUtil.getRand().random(1, 10);
                bullet.puddleRange = RandomUtil.getRand().random(1f, 10f);
                bullet.puddleAmount = RandomUtil.getRand().random(5f, 20f);
                bullet.puddleLiquid = content.liquids().random(RandomUtil.getRand());
            }
            bullet.init();
            bullet.rangeOverride = getRange(bullet);
        });
    }

    public static float getRange(BulletType b) {
        if(b.rangeOverride > 0) return b.rangeOverride;
        if(b.spawnUnit != null) return b.spawnUnit.lifetime * b.spawnUnit.speed;
        return b.fragBullet != null ? getRange(b.fragBullet) : 0 + Math.max(Mathf.zero(b.drag) ? b.speed * b.lifetime : b.speed * (1f - Mathf.pow(1f - b.drag, b.lifetime)) / b.drag, b.maxRange);
    }
}
