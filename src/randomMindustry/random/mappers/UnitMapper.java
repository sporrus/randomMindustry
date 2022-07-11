package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;

import randomMindustry.random.util.*;
import randomMindustry.util.*;

import static mindustry.Vars.*;

// oh god no
public class UnitMapper{
    public static void init(){
        Seq<UnitType> units = content.units().copy();
        RandomUtil.shuffle(units);
        units.each(UnitMapper::modify);
    }
    
    public static void modify(UnitType unit){
        Seq<Effect> effects = Effect.all.select(effect -> effect != Fx.dynamicExplosion);
        
        if(!unit.flying && !unit.naval){
            unit.canBoost = RandomUtil.getRand().random(-2f, 2f) > 0;
            unit.boostMultiplier = RandomUtil.getRand().random(5f);
        }
        
        if(!unit.naval) unit.canDrown = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.createWreck = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.createScorch = RandomUtil.getRand().random(-2f, 2f) > 0;
        
        unit.legCount = RandomUtil.getRandomIntMult(2, 10, 2);
        unit.legGroupSize = RandomUtil.getRandomIntMult(2, 10, 2);
        unit.legLength = RandomUtil.getRand().random(1f, 25f);
        unit.legSpeed = RandomUtil.getRand().random(0.1f, 3f);
        unit.legStraightness = RandomUtil.getRand().random(1f);
        unit.lockLegBase = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.legContinuousMove = RandomUtil.getRand().random(-2f, 2f) > 0;
        
        unit.deathSound = Util.generateSound();
        unit.mineSound = Util.generateSound();
        
        unit.fallEffect = effects.random(RandomUtil.getRand());
        unit.fallEngineEffect = effects.random(RandomUtil.getRand());
        unit.deathExplosionEffect = effects.random(RandomUtil.getRand());
        
        unit.weapons.each(weapon -> {
            weapon.shootSound = Util.generateSound();
            weapon.chargeSound = Util.generateSound();
            weapon.noAmmoSound = Util.generateSound();
            weapon.reload = RandomUtil.getRand().random(100f);
            weapon.shootCone = RandomUtil.getRand().random(1f, 360f);
            weapon.inaccuracy = RandomUtil.getRand().random(1f, 180f);
            weapon.xRand = RandomUtil.getRand().random(1f, 10f);
            weapon.shootX = RandomUtil.getRand().random(1f, 20f);
            weapon.shootY = RandomUtil.getRand().random(1f, 20f);
            weapon.x = RandomUtil.getRand().random(1f, 20f);
            weapon.y = RandomUtil.getRand().random(1f, 20f);
            weapon.recoil = RandomUtil.getRand().random(-50f, 50f);
            weapon.rotateSpeed = RandomUtil.getRand().random(1f, 20f);
            weapon.rotationLimit = RandomUtil.getRand().random(45f, 361f);
            
            weapon.bullet = content.bullets().random(RandomUtil.getRand());
            
            int pattern = RandomUtil.getRand().random(0, 5);
            switch (pattern) {
                case 0 -> weapon.shoot = new ShootPattern();
                case 1 -> weapon.shoot = new ShootAlternate(RandomUtil.getRand().random(1f, 15f)) {{
                    barrels = RandomUtil.getRand().random(1, 5);
                }};
                case 2 -> weapon.shoot = new ShootBarrel() {{
                    int barrelAmount = RandomUtil.getRand().random(1, 10);
                    float[] barrelArray = new float[barrelAmount * 3];
                    for (int i = 0; i < barrelArray.length; i += 3) {
                        barrelArray[i] = RandomUtil.getRand().random(-10f, 10f);
                        barrelArray[i + 1] = RandomUtil.getRand().random(-10f, 10f);
                        barrelArray[i + 2] = RandomUtil.getRand().random(0, 360f);
                    }
                    barrels = barrelArray;
                }};
                case 3 -> weapon.shoot = new ShootHelix() {{
                    scl = RandomUtil.getRand().random(1f, 20f);
                    mag = RandomUtil.getRand().random(1f, 10f);
                }};
                case 4 -> weapon.shoot = new ShootSine(RandomUtil.getRand().random(1f, 20f), RandomUtil.getRand().random(1f, 10f));
                case 5 -> weapon.shoot = new ShootSpread() {{
                    spread = RandomUtil.getRand().random(1f, 15f);
                }};
            }
            weapon.shoot.shots = RandomUtil.getRand().random(1, 10);
            weapon.shoot.shotDelay = RandomUtil.getRand().random(1f, 60f);
        });
    }
}
