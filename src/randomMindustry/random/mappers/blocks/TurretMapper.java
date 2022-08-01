package randomMindustry.random.mappers.blocks;

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
        if(!headless) block.localizedName = StringGenerator.generateTurretName();
        block.shootSound = Util.generateSound();
        block.loopSound = Util.generateSound();
        block.chargeSound = Util.generateSound();
        block.reload = RandomUtil.getRand().random(100f);
        block.shootCone = RandomUtil.getRand().random(1f, 360f);
        block.inaccuracy = RandomUtil.getRand().random(1f, 180f);
        block.xRand = RandomUtil.getRand().random(1f, 10f);
        block.shootX = RandomUtil.getRand().random(-block.size * tilesize / 2f, block.size * tilesize / 2f);
        block.shootY = RandomUtil.getRand().random(-block.size * tilesize / 2f, block.size * tilesize / 2f);
        block.recoil = RandomUtil.getRand().random(-25f, 25f);
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
        turret.shootType = content.bullets().select(b -> !(b instanceof MassDriverBolt)).random(RandomUtil.getRand());
        turret.targetAir = turret.shootType.collidesAir;
        turret.targetGround = turret.shootType.collidesGround;
        turret.targetHealing = turret.shootType.healPercent > 0;
        turret.range = BulletMapper.getRange(turret.shootType);
        
        if(!headless){
            if(!(turret.drawer instanceof DrawTurret drawer)) return;
            
            float hue = RandomUtil.getClientRand().random(360f);
            TextureGenerator.changeHue(turret.region, hue);
            TextureGenerator.changeHue(drawer.preview, hue);
            TextureGenerator.changeHue(drawer.base, hue);
            TextureGenerator.changeHue(drawer.top, hue);
            TextureGenerator.changeHue(drawer.heat, hue);
            TextureGenerator.changeHue(turret.fullIcon, hue);
            TextureGenerator.changeHue(turret.uiIcon, hue);
            
            turret.heatColor.hue(hue);
            
            drawer.parts.each(part -> huePart(part, hue));
        }
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
        turret.ammo(ammo.toArray());
        
        if(!headless){
            if(!(turret.drawer instanceof DrawTurret drawer)) return;
            
            float hue = RandomUtil.getClientRand().random(360f);
            TextureGenerator.changeHue(turret.region, hue);
            TextureGenerator.changeHue(drawer.preview, hue);
            TextureGenerator.changeHue(drawer.base, hue);
            TextureGenerator.changeHue(drawer.top, hue);
            TextureGenerator.changeHue(drawer.heat, hue);
            TextureGenerator.changeHue(turret.fullIcon, hue);
            TextureGenerator.changeHue(turret.uiIcon, hue);
            
            turret.heatColor.hue(hue);
            
            drawer.parts.each(part -> huePart(part, hue));
        }
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
        turret.ammo(ammo.toArray());
        
        if(!headless){
            if(!(turret.drawer instanceof DrawTurret drawer)) return;
            
            float hue = RandomUtil.getClientRand().random(360f);
            TextureGenerator.changeHue(turret.region, hue);
            TextureGenerator.changeHue(drawer.preview, hue);
            TextureGenerator.changeHue(drawer.base, hue);
            TextureGenerator.changeHue(drawer.top, hue);
            TextureGenerator.changeHue(drawer.heat, hue);
            TextureGenerator.changeHue(turret.fullIcon, hue);
            TextureGenerator.changeHue(turret.uiIcon, hue);
            
            turret.heatColor.hue(hue);
            
            drawer.parts.each(part -> huePart(part, hue));
        }
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
        turret.ammo(ammo.toArray());
        
        if(!headless){
            if(!(turret.drawer instanceof DrawTurret drawer)) return;
            
            Item item = turret.ammoTypes.keys().toSeq().random(RandomUtil.getClientRand());
            TextureGenerator.changeHue(turret.region, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(drawer.preview, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(drawer.base, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(drawer.top, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(drawer.heat, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(turret.fullIcon, ItemMapper.hues.get(item));
            TextureGenerator.changeHue(turret.uiIcon, ItemMapper.hues.get(item));
            
            turret.heatColor.hue(ItemMapper.hues.get(item));
            
            drawer.parts.each(part -> huePart(part, ItemMapper.hues.get(item)));
        }
    }
    
    public static void huePart(DrawPart part, float hue){
        if(part instanceof RegionPart regionpart){
            TextureGenerator.changeHue(regionpart.heat, hue);
            
            for(TextureRegion region : regionpart.regions) TextureGenerator.changeHue(region, hue);
            
            regionpart.color.hue(hue);
            regionpart.colorTo.hue(hue);
            regionpart.mixColor.hue(hue);
            regionpart.mixColorTo.hue(hue);
            regionpart.heatColor.hue(hue);
            
            regionpart.children.each(child -> huePart(child, hue));
        }
        if(part instanceof HaloPart halopart){
            halopart.color.hue(hue);
            halopart.colorTo.hue(hue);
        }
    }
}
