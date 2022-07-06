package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.type.*;
import mindustry.content.*;
import mindustry.entities.*;

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
        
        if(!unit.flying){
            unit.canBoost = RandomUtil.getRand().random(-2f, 2f) > 0;
            unit.boostMultiplier = RandomUtil.getRand().random(5f);
        }
        
        unit.canDrown = RandomUtil.getRand().random(-2f, 2f) > 0;
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
    }
}
