package randomMindustry;

import arc.util.*;
import arc.struct.*;
import arc.math.*;
import mindustry.gen.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;

import static mindustry.Vars.*;

public class BulletMapper{
    public static void init(){
        Seq<Effect> effects = Effect.all;
        effects.remove(Fx.dynamicExplosion);
        
        // Currently only visual, will be fixed later on.
        content.bullets().each(b -> {
            b.hitSound = Sounds.getSound(ResourceMapper.getRandomInt(0, 71));
            b.despawnSound = Sounds.getSound(ResourceMapper.getRandomInt(0, 71));
            
            // some fx crash game
            /*b.shootEffect = effects.random(Main.rand);
            b.smokeEffect = effects.random(Main.rand);
            b.chargeEffect = effects.random(Main.rand);
            b.hitEffect = effects.random(Main.rand);
            b.despawnEffect = effects.random(Main.rand);
            b.trailEffect = effects.random(Main.rand);
            b.healEffect = effects.random(Main.rand);*/
        });
    }
}
