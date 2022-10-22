package randomMindustry.mappers.bullet;

import arc.struct.*;
import mindustry.entities.bullet.*;
import mindustry.world.blocks.defense.turrets.*;
import randomMindustry.mappers.bullet.types.*;
import randomMindustry.random.*;

public class BulletMapper{
    public static Seq<RandomBullet> generatedBullets = new Seq<>();
    public static int lastId = 0;
    public static SyncedRand r = new SyncedRand();

    public static BulletType random(int tier, Turret turret) {
        RandomBullet bullet;
        if (generatedBullets.size > lastId) {
            bullet = generatedBullets.get(lastId++);
            bullet.generate(tier, turret);
        } else {
            bullet = new RandomBasicBullet(lastId++, tier, turret);
            generatedBullets.add(bullet);
        }
        return (BulletType) bullet;
    }
}
