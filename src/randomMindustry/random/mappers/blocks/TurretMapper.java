package randomMindustry.random.mappers.blocks;

import arc.struct.Seq;
import mindustry.content.Planets;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.pattern.*;
import mindustry.gen.Sounds;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeCoolant;
import randomMindustry.random.mappers.ResourceMapper;
import randomMindustry.random.util.RandomUtil;
import randomMindustry.util.Util;
import randomMindustry.util.techTrees.TechUtil;

import static mindustry.Vars.content;

public class TurretMapper {
    public static void map(Turret block) {
        block.shootSound = Sounds.getSound(RandomUtil.getRand().random(0, 71));
        block.loopSound = Sounds.getSound(RandomUtil.getRand().random(0, 71));
        block.reload = RandomUtil.getRand().random(100f);

        int pattern = RandomUtil.getRand().random(0, 5);
        switch (pattern) {
            case 0 -> block.shoot = new ShootPattern();
            case 1 -> block.shoot = new ShootAlternate(RandomUtil.getRand().random(1f, 15f)) {{
                barrels = RandomUtil.getRand().random(1, 5);
            }};
            case 2 -> block.shoot = new ShootBarrel() {{
                int barrelAmount = RandomUtil.getRand().random(1, 10);
                float[] barrelArray = new float[barrelAmount * 3];
                for (int i = 0; i < barrelArray.length; i += 3) {
                    barrelArray[i] = RandomUtil.getRand().random(-10f, 10f);
                    barrelArray[i + 1] = RandomUtil.getRand().random(-10f, 10f);
                    barrelArray[i + 2] = RandomUtil.getRand().random(0, 360f);
                }
                barrels = barrelArray;
            }};
            case 3 -> block.shoot = new ShootHelix() {{
                scl = RandomUtil.getRand().random(1f, 20f);
                mag = RandomUtil.getRand().random(1f, 10f);
            }};
            case 4 -> block.shoot = new ShootSine(RandomUtil.getRand().random(1f, 20f), RandomUtil.getRand().random(1f, 10f));
            case 5 -> block.shoot = new ShootSpread() {{
                spread = RandomUtil.getRand().random(1f, 15f);
            }};
        }
        block.shoot.shots = RandomUtil.getRand().random(1, 10);
        block.shoot.shotDelay = RandomUtil.getRand().random(1f, 60f);

        if (block instanceof ItemTurret turret) modifyItemTurret(turret);
        else if (block instanceof LiquidTurret turret) modifyLiquidTurret(turret);
        else if (block instanceof ContinuousLiquidTurret turret) modifyContinuousLiquidTurret(turret);
        else if (block instanceof PowerTurret turret) modifyPowerTurret(turret);

        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
    }

    public static void modifyPowerTurret(PowerTurret turret) {
        turret.consumePower(RandomUtil.getRand().random(20000) / 1000f);
        turret.shootType = content.bullets().random(RandomUtil.getRand());
    }

    public static void modifyContinuousLiquidTurret(ContinuousLiquidTurret turret) {
        Seq<Object> ammo = new Seq<>();
        Seq<Liquid> liquids = content.liquids().select((liquid -> TechUtil.getRoot(liquid).contains(Planets.serpulo)));
        Seq<BulletType> bullets = content.bullets();
        int count = RandomUtil.getRand().random(1, 5);
        for (int i = 0; i < count; i++)
            ammo.add(liquids.random(RandomUtil.getRand()), bullets.random(RandomUtil.getRand()));
        Seq<Consume> nonOptionalConsumers = new Seq<>(turret.nonOptionalConsumers);
        Util.removeConsumers(turret, nonOptionalConsumers::contains);
        turret.ammo(ammo.toArray());
    }

    public static void modifyLiquidTurret(LiquidTurret turret) {
        Seq<Object> ammo = new Seq<>();
        Seq<Liquid> liquids = content.liquids().select((liquid -> TechUtil.getRoot(liquid).contains(Planets.serpulo)));
        Seq<BulletType> bullets = content.bullets();
        int count = RandomUtil.getRand().random(1, 5);
        for (int i = 0; i < count; i++)
            ammo.add(liquids.random(RandomUtil.getRand()), bullets.random(RandomUtil.getRand()));
        Seq<Consume> nonOptionalConsumers = new Seq<>(turret.nonOptionalConsumers);
        Util.removeConsumers(turret, nonOptionalConsumers::contains);
        turret.ammo(ammo.toArray());
    }

    public static void modifyItemTurret(ItemTurret turret) {
        Seq<Object> ammo = new Seq<>();
        Seq<Item> items = content.items().select((item -> TechUtil.getRoot(item).contains(Planets.serpulo)));
        Seq<BulletType> bullets = content.bullets();
        int count = RandomUtil.getRand().random(1, 5);
        for (int i = 0; i < count; i++)
            ammo.add(items.random(RandomUtil.getRand()), bullets.random(RandomUtil.getRand()));
        Seq<Consume> nonOptionalConsumers = new Seq<>(turret.nonOptionalConsumers);
        Util.removeConsumers(turret, nonOptionalConsumers::contains);
        turret.ammo(ammo.toArray());
    }
}
