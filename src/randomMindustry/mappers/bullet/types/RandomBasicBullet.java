package randomMindustry.mappers.bullet.types;

import arc.graphics.*;
import mindustry.entities.bullet.*;
import randomMindustry.mappers.item.*;

import mindustry.entities.bullet.BulletMapper.r;

public class RandomBasicBullet extends BasicBulletType implements RandomBullet{
    public final int id;
    public boolean
    randomizeColor = true,
    isUsed = false;
    
    public RandomBasicBullet(float id){
        super(0f, 0f);
        this.id = id;
        gen();
    }
    
    @Override
    public int tier(){
        return id + 1;
    }
    
    @Override
    public int tier2(){
        return tier() < 3 ? 1 : (int)Math.round(tier()/3);
    }
    
    @Override
    public void gen(){
        if(randomizeColor){
            Color color = new Color(r.random(0.6f, 1f), r.random(0.6f, 1f), r.random(0.6f, 1f));
            backColor = trailColor = hitColor = healColor = lightningColor = color;
            frontColor = color.cpy().mul(1.5f);
        }
        damage = (float)Math.floor(tier2() * r.random(1f, 10f) + r.random(-tier2(), tier2()) / 4f);
        speed = r.random(1f, 10f) * tier2();
        width = r.random(10f, 20f);
        height = r.random(width + 5, width + 20);
        
        if(r.chance(0.5f)){
            trailWidth = width;
            trailLength = r.random(height * 2.5f, height * 4.5f) * tier();
        }else{
            trailWidth = trailLength = 0;
        }
        
        if(r.chance(0.35f)) homingPower = r.random(0.025f, 0.2f) * tier2();
        else homingPower = 0;
        
        if(r.chance(0.35f)){
            splashDamage = damage * r.random(0.5f, 2f) * tier2();
            splashDamageRadius = r.random(8f, 40f) * tier2();
        }else{
            splashDamage = 0;
            splashDamageRadius = -1f;
        }
        
        if(r.chance(0.35f)){
            lightning = r.random(1, 3) * tier2();
            lightningLength = r.random(2, 10) * tier2();
            lightningDamage = damage * r.random(0.25f, 0.5f) * tier2();
        }else{
            lightning = lightningLength = lightningDamage = 0;
        }
        
        if(r.chance(0.35f)) healPercent = r.random(5f, 10f) * tier2();
        else healPercent = 0;
        
        if(r.chance(0.35f)){
            weaveScale = r.random(5f, 10f);
            weaveMag = r.random(1f, 5f);
        }else{
            weaveScale = weaveMag = 0;
        }
        
        scaleLife = r.chance(0.25f);
    }
}
