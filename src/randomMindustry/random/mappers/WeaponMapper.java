package randomMindustry.random.mappers;

import mindustry.entities.pattern.*;
import mindustry.type.Weapon;
import mindustry.world.blocks.defense.turrets.Turret;
import randomMindustry.random.util.RandomUtil;
import randomMindustry.util.Util;

import static mindustry.Vars.content;

public class WeaponMapper {
    public static void map(Weapon weapon) {
        weapon.shootSound = Util.generateSound();
        weapon.chargeSound = Util.generateSound();
        weapon.noAmmoSound = Util.generateSound();
        weapon.reload = RandomUtil.getRand().random(100f);
        weapon.shootCone = RandomUtil.getRand().random(1f, 360f);
        weapon.inaccuracy = RandomUtil.getRand().random(1f, 180f);
        weapon.xRand = RandomUtil.getRand().random(1f, 10f);
        weapon.shootX = RandomUtil.getRand().random(-20f, 20f);
        weapon.shootY = RandomUtil.getRand().random(-20f, 20f);
        weapon.x = RandomUtil.getRand().random(-30f, 30f);
        weapon.y = RandomUtil.getRand().random(-30f, 30f);
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
    }
}
