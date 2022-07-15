package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.type.*;
import mindustry.type.unit.*;
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
        Seq<Effect> effects = FxMapper.effects;

        if(!unit.flying && !unit.naval){
            unit.canBoost = RandomUtil.getRand().random(-2f, 2f) > 0;
            unit.boostMultiplier = RandomUtil.getRand().random(5f);
        }
        
        if(!unit.naval) unit.canDrown = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.createWreck = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.createScorch = RandomUtil.getRand().random(-2f, 2f) > 0;
        
        unit.legCount = RandomUtil.getRandomIntMult(2, 10, 2);
        unit.legGroupSize = unit.legCount / RandomUtil.getRand().random(1, 3);
        unit.legLength = RandomUtil.getRand().random(-70f, 70f);
        unit.legSpeed = RandomUtil.getRand().random(0.1f, 3f);
        unit.legStraightness = RandomUtil.getRand().random(1f);
        unit.lockLegBase = RandomUtil.getRand().random(-2f, 2f) > 0;
        unit.legContinuousMove = RandomUtil.getRand().random(-2f, 2f) > 0;
        
        unit.deathSound = Util.generateSound();
        unit.mineSound = Util.generateSound();
        
        unit.fallEffect = effects.random(RandomUtil.getClientRand());
        unit.fallEngineEffect = effects.random(RandomUtil.getClientRand());
        unit.deathExplosionEffect = effects.random(RandomUtil.getClientRand());
        
        if(unit instanceof MissileUnitType) return;
        unit.weapons.each(WeaponMapper::map);
    }
}
