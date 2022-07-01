package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.gen.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;

import randomMindustry.random.util.*;

import static mindustry.Vars.*;

public class BulletMapper{
    public static void init(){
        Seq<Effect> effects = Effect.all.select(effect -> effect != Fx.dynamicExplosion);
        
        Seq<BulletType> bullets = content.bullets();
        RandomUtil.shuffle(bullets);
        bullets.each(bullet -> {
            bullet.hitSound = Sounds.getSound(RandomUtil.getRand().random(0, 71));
            bullet.despawnSound = Sounds.getSound(RandomUtil.getRand().random(0, 71));
            
            bullet.hitEffect = effects.random(RandomUtil.getRand());
            bullet.despawnEffect = effects.random(RandomUtil.getRand());
            bullet.shootEffect = effects.random(RandomUtil.getRand());
            bullet.chargeEffect = effects.random(RandomUtil.getRand());
            bullet.smokeEffect = effects.random(RandomUtil.getRand());
            bullet.trailEffect = effects.random(RandomUtil.getRand());
            bullet.healEffect = effects.random(RandomUtil.getRand());
            
            bullet.weaveScale = RandomUtil.getRand().random(0.5f, 20f);
            bullet.weaveMag = RandomUtil.getRand().random(0.5f, 10f);
            
            bullet.rangeChange = RandomUtil.getRand().random(0.5f, 20f);
            bullet.scaleLife = RandomUtil.getRand().random(-2f, 2f) < 0f ? true : false;
            bullet.healPercent = RandomUtil.getRand().random(0f, 100f);
            if(bullet.healPercent > 0) bullet.collidesTiles = bullet.collidesTeam = bullet.collides = true;
        });
    }
}
