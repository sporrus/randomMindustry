package randomMindustry;

import static mindustry.Vars.*;

public class BulletMapper{
    // TODO: Apply settings.
    public static void init(){
        content.units().each(u -> {
            u.weapons.each(w -> {
                w.bullet = content.bullets().random(Main.rand);
            });
        });
    }
}
