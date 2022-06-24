package randomMindustry;

import arc.*;
import arc.struct.*;
import mindustry.*;

import static mindustry.Vars.*;

public class BulletMapper{
    // TODO: Apply settings.
    public static void init(){
        content.units().each(u -> {
            u.weapons.each(w -> {
                w.bullet = content.bullets().random();
            });
        });
        
        // TODO: Randomize ammoTypes as well?
        content.blocks().each(b -> {
            if(b.shootType != null){
                b.shootType = content.bullets().random();
            }
        });
    }
}