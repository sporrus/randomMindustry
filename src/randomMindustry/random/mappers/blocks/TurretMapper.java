package randomMindustry.random.mappers.blocks;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.*;
import mindustry.entities.part.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.*;
import mindustry.world.draw.*;
import randomMindustry.random.mappers.*;
import randomMindustry.random.util.*;
import randomMindustry.util.*;
import randomMindustry.util.techTrees.*;

import static mindustry.Vars.*;

public class TurretMapper {
    public static void map(Turret block) {
        if(!headless && Core.settings.getBool("rm-name-random")) block.localizedName = StringGenerator.generateTurretName();
        block.shootSound = Util.generateSound();
        block.loopSound = Util.generateSound();
        block.chargeSound = Util.generateSound();
        block.reload = RandomUtil.getRand().random(100f);
        block.shootCone = RandomUtil.getRand().random(1f, 360f);
        block.inaccuracy = RandomUtil.getRand().random(1f, 180f);
        block.rotateSpeed = RandomUtil.getRand().random(1f, 20f);

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

        block.targetAir = block.targetGround = block.targetHealing = false;

        if (block instanceof ItemTurret turret) modifyItemTurret(turret);
        else if (block instanceof LiquidTurret turret) modifyLiquidTurret(turret);
        else if (block instanceof ContinuousLiquidTurret turret) modifyContinuousLiquidTurret(turret);
        else if (block instanceof PowerTurret turret) modifyPowerTurret(turret);

        block.requirements = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(4) + 1, 4, (int) Math.floor(block.health / 4d), 5, true);
    }

    public static void modifyPowerTurret(PowerTurret turret) {
        turret.consumePower(RandomUtil.getRand().random(20000) / 1000f);
        Seq<Consume> nonOptionalConsumers = new Seq<>(turret.nonOptionalConsumers);
        Util.removeConsumers(turret, nonOptionalConsumers::contains);
        Util.removeBars(turret);
        turret.shootType = content.bullets().select(b -> !(b instanceof MassDriverBolt)).random(RandomUtil.getRand());
        turret.targetAir = turret.shootType.collidesAir;
        turret.targetGround = turret.shootType.collidesGround;
        turret.targetHealing = turret.shootType.healPercent > 0;
        turret.range = BulletMapper.getRange(turret.shootType);

        modifyTurretSprite(turret);
    }

    public static void modifyContinuousLiquidTurret(ContinuousLiquidTurret turret) {
        Seq<Object> ammo = new Seq<>();
        Seq<Liquid> liquids = content.liquids().select((liquid -> TechUtil.getRoot(liquid).contains(Planets.serpulo)));
        Seq<BulletType> bullets = content.bullets().select(b -> !(b instanceof MassDriverBolt));
        int count = RandomUtil.getRand().random(1, 5);
        int sum = 0;
        turret.targetAir = turret.targetGround = false;
        for (int i = 0; i < count; i++) {
            BulletType bullet = bullets.random(RandomUtil.getRand());
            sum += BulletMapper.getRange(bullet);
            turret.targetAir |= bullet.collidesAir;
            turret.targetGround |= bullet.collidesGround;
            turret.targetHealing |= bullet.healPercent > 0;
            ammo.add(liquids.random(RandomUtil.getRand()), bullet);
        }
        turret.range = sum / (float)count;
        Seq<Consume> nonOptionalConsumers = new Seq<>(turret.nonOptionalConsumers);
        Util.removeConsumers(turret, nonOptionalConsumers::contains);
        Util.removeBars(turret);
        turret.ammo(ammo.toArray());

        modifyTurretSprite(turret);
    }

    // TODO: crap i forgor to make v1.5.0 for liquid turrets too bad
    public static void modifyLiquidTurret(LiquidTurret turret) {
        Seq<Object> ammo = new Seq<>();
        Seq<Liquid> liquids = content.liquids().select((liquid -> TechUtil.getRoot(liquid).contains(Planets.serpulo)));
        Seq<BulletType> bullets = content.bullets().select(b -> !(b instanceof MassDriverBolt));
        int count = RandomUtil.getRand().random(1, 5);
        int sum = 0;
        turret.targetAir = turret.targetGround = false;
        for (int i = 0; i < count; i++) {
            BulletType bullet = bullets.random(RandomUtil.getRand());
            sum += BulletMapper.getRange(bullet);
            turret.targetAir |= bullet.collidesAir;
            turret.targetGround |= bullet.collidesGround;
            turret.targetHealing |= bullet.healPercent > 0;
            ammo.add(liquids.random(RandomUtil.getRand()), bullet);
        }
        turret.range = sum / (float)count;
        Seq<Consume> nonOptionalConsumers = new Seq<>(turret.nonOptionalConsumers);
        Util.removeConsumers(turret, nonOptionalConsumers::contains);
        Util.removeBars(turret);
        turret.ammo(ammo.toArray());

        modifyTurretSprite(turret);
    }

    public static void modifyItemTurret(ItemTurret turret) {
        Seq<Object> ammo = new Seq<>();
        Seq<Item> items = ItemMapper.getSelectedItems().copy();
        Seq<BulletType> bullets = content.bullets().select(b -> !(b instanceof MassDriverBolt));
        int count = RandomUtil.getRand().random(1, 5);
        int sum = 0;
        turret.targetAir = turret.targetGround = false;
        for (int i = 0; i < count; i++) {
            BulletType bullet = bullets.random(RandomUtil.getRand());
            sum += BulletMapper.getRange(bullet);
            turret.targetAir |= bullet.collidesAir;
            turret.targetGround |= bullet.collidesGround;
            turret.targetHealing |= bullet.healPercent > 0;
            ammo.add(items.random(RandomUtil.getRand()), bullet);
        }
        turret.range = sum / (float)count;
        Seq<Consume> nonOptionalConsumers = new Seq<>(turret.nonOptionalConsumers);
        Util.removeConsumers(turret, nonOptionalConsumers::contains);
        Util.removeBars(turret);
        turret.ammo(ammo.toArray());
        
        if(!headless && Core.settings.getBool("rm-sprite-random")){
            if(!(turret.drawer instanceof DrawTurret drawer)) return;
            
            Item item = turret.ammoTypes.keys().toSeq().random(RandomUtil.getClientRand());
            TextureGenerator.changeHue(turret.region, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(drawer.preview, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(drawer.base, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(drawer.top, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(drawer.heat, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(turret.fullIcon, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(turret.uiIcon, ItemMapper.hues.get(item));

            turret.heatColor = turret.heatColor.cpy().hue(ItemMapper.hues.get(item));
            
            drawer.parts.each(part -> huePart(part, ItemMapper.hues.get(item)));
        }
    }
    
    public static void huePart(DrawPart part, float hue){
        if(part instanceof RegionPart regionpart){
            TextureGenerator.changeHue(regionpart.heat, hue);
            
            for(TextureRegion region : regionpart.regions) TextureGenerator.changeHue(region, hue);
            
            if(regionpart.color != null) regionpart.color = regionpart.color.cpy().hue(hue);
            if(regionpart.colorTo != null) regionpart.colorTo = regionpart.colorTo.cpy().hue(hue);
            if(regionpart.mixColor != null) regionpart.mixColor = regionpart.mixColor.cpy().hue(hue);
            if(regionpart.mixColorTo != null) regionpart.mixColorTo = regionpart.mixColorTo.cpy().hue(hue);
            regionpart.heatColor = regionpart.heatColor.cpy().hue(hue);
            
            regionpart.children.each(child -> huePart(child, hue));
        }
        if(part instanceof HaloPart halopart){
            halopart.color = halopart.color.cpy().hue(hue);
            if(halopart.colorTo != null) halopart.colorTo = halopart.colorTo.cpy().hue(hue);
        }
        if(part instanceof ShapePart shapepart){
            shapepart.color = shapepart.color.cpy().hue(hue);
            if(shapepart.colorTo != null) shapepart.colorTo = shapepart.colorTo.cpy().hue(hue);
        }
        if(part instanceof FlarePart flarepart){
            flarepart.color1 = flarepart.color1.cpy().hue(hue);
            flarepart.color2 = flarepart.color2.cpy().hue(hue);
        }
        if(part instanceof HoverPart hoverpart) hoverpart.color = hoverpart.color.cpy().hue(hue);
    }

    public static void modifyTurretSprite(Turret turret) {
        if(!headless && Core.settings.getBool("rm-sprite-random")){
            if(!(turret.drawer instanceof DrawTurret drawer)) return;

            float hue = RandomUtil.getClientRand().random(360f);
            TextureGenerator.changeHue(turret.region, hue);
            TextureGenerator.changeHue(drawer.preview, hue);
            TextureGenerator.changeHue(drawer.base, hue);
            TextureGenerator.changeHue(drawer.top, hue);
            TextureGenerator.changeHue(drawer.heat, hue);
            TextureGenerator.changeHue(turret.fullIcon, hue);
            TextureGenerator.changeHue(turret.uiIcon, hue);

            turret.heatColor = turret.heatColor.cpy().hue(hue);

            drawer.parts.each(part -> huePart(part, hue));
        }
    }
}
