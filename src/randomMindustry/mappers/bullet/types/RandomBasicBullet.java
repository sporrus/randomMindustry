package randomMindustry.mappers.bullet.types;

import mindustry.entities.bullet.*;
import mindustry.world.blocks.defense.turrets.*;

import static randomMindustry.mappers.bullet.BulletMapper.*;

public class RandomBasicBullet extends BasicBulletType implements RandomBullet{
    public final int id;
    public int tier;
    public Turret turret;
    
    public RandomBasicBullet(int id, int tier, Turret turret){
        super(0f, 0f);
        this.id = id;
        this.tier = tier;
        this.turret = turret;
        generate(tier, turret);
    }
    
    @Override
    public void generate(int tier, Turret turret) {
        this.tier = tier;
        this.turret = turret;

        damage = (float)Math.floor(tier * r.random(1f, 10f) + r.random(-tier, tier) / 4f);
        speed = r.random(1f, 10f) * tier;
        width = r.random(10f, 20f);
        height = r.random(width + 5, width + 20);
        
        if (r.chance(0.5f)) {
            trailWidth = width;
            trailLength = (int)r.random(height * 2.5f, height * 4.5f);
        } else trailWidth = trailLength = 0;
        
        if (r.chance(0.35f)) homingPower = r.random(0.025f, 0.2f) * tier;
        else homingPower = 0;
        
        if (r.chance(0.35f)) {
            splashDamage = damage * r.random(0.25f, 1f);
            splashDamageRadius = r.random(8f, 40f);
        } else {
            splashDamage = 0;
            splashDamageRadius = -1f;
        }
        
        if (r.chance(0.35f)) {
            lightning = r.random(1, 3);
            lightningLength = r.random(2, 10);
            lightningDamage = damage * r.random(0.25f, 0.5f);
        } else lightningDamage = lightning = lightningLength = 0;
        
        if (r.chance(0.35f)) healAmount = r.random(0.25f, 0.75f) * damage;
        else healAmount = 0;
        
        if (r.chance(0.35f)) {
            weaveScale = r.random(5f, 10f);
            weaveMag = r.random(1f, 5f);
        } else weaveScale = weaveMag = 0;
        
        scaleLife = r.chance(0.25f);
    }
}
