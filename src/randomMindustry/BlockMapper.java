package randomMindustry;

import arc.assets.loaders.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.units.*;
import mindustry.world.blocks.units.UnitFactory.*;
import mindustry.world.consumers.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class BlockMapper {
    public static void init() {
        Seq<Block> blocks = content.blocks().copy();
        Main.shuffle(blocks);
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block) {
        Seq<Category> cats = new Seq<>();
        cats.addAll(Category.all);
        if (settings.getBool("rmchaos-category-rand", false)) block.category = cats.random(Main.rand);
        if (!Main.getRoot(block).contains(Planets.serpulo)) return;
        if (block instanceof GenericCrafter) {
            modifyCrafter((GenericCrafter) block);
        } else if (block instanceof Drill) {
            modifyDrill((Drill) block);
        } else if (block instanceof Conveyor) {
            modifyConveyor((Conveyor) block);
        } else if (block instanceof Wall) {
            modifyWall((Wall) block);
        } else if (block instanceof Turret) {
            modifyTurret((Turret) block);
        } else if (block instanceof UnitFactory) {
            modifyUnitFactory((UnitFactory) block);
        } else {
            modifyBlock(block);
        }
        
        block.init();
    }

    public static void modifyTurret(Turret block) {
        block.shootSound = Sounds.getSound(ResourceMapper.getRandomInt(0, 71));
        if (block instanceof ItemTurret) {
            Seq<Object> ammo = new Seq<>();
            Seq<Item> items = content.items().select((item -> Main.getRoot(item).contains(Planets.serpulo)));
            Seq<BulletType> bullets = content.bullets();
            int count = ResourceMapper.getRandomInt(1, 5);
            for (int i = 0; i < count; i++) {
                ammo.add(items.random(Main.rand), bullets.random(Main.rand));
            }
            ((ItemTurret) block).ammo(ammo.toArray());
        } else if (block instanceof LiquidTurret) {
            Seq<Object> ammo = new Seq<>();
            Seq<Liquid> items = content.liquids().select((liquid -> Main.getRoot(liquid).contains(Planets.serpulo)));
            Seq<BulletType> bullets = content.bullets();
            int count = ResourceMapper.getRandomInt(1, 5);
            for (int i = 0; i < count; i++) {
                ammo.add(items.random(Main.rand), bullets.random(Main.rand));
            }
            ((LiquidTurret) block).ammo(ammo.toArray());
        } else if (block instanceof LaserTurret) {
            ((LaserTurret) block).shootType = content.bullets().random(Main.rand);
            block.consumePower(ResourceMapper.getRandomInt(20000) / 1000f);
        }
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 5, (int) (block.health / 2), 5, true);
    }

    public static void modifyCrafter(GenericCrafter block) {
        Item item = ResourceMapper.getGenericCrafterOut();
        if (item == null) item = ResourceMapper.getRandomItem(false);
        int count = ResourceMapper.getRandomInt(10) + 1;
        block.outputItems = new ItemStack[]{new ItemStack(
                item, count
        )};
        int tier = ResourceMapper.getTierOfItem(item);
        Consume[] save = block.consumers;
        block.consumers = new Consume[0];
        for (Consume consume : save) block.removeConsumer(consume);
        block.consumeItems(ResourceMapper.getRandomItemStacks(tier, 3, 10, 1, true));
        block.requirements = ResourceMapper.getRandomItemStacks(tier, 5, (int) (block.health / 2), 5, true);
    }

    public static void modifyConveyor(Conveyor block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 3, (int) (block.health / 4), 1, true);
    }

    public static void modifyBlock(Block block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 5, (int) (block.health / 2), 5, true);
    }

    public static void modifyDrill(Drill block) {
        int localTier = block.tier * 2 - 3;
        block.requirements = ResourceMapper.getRandomItemStacks(localTier, 5, (int) (block.health / 2), 5, true);
    }

    public static void modifyWall(Wall block) {
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 5, block.size * 10, 5, true);
    }
    
    public static void modifyUnitFactory(UnitFactory block){
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 5, (int) (block.health / 2), 5, true);
        
        Seq<UnitPlan> plans = block.plans;
        
        plans.each(plan -> {
            // randomize plan build time and unit?
            plan.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(3) + 1, 5, (int) (plan.unit.health / 2), 5, true);
        });
    }
    
    public static void modifyReconstructor(Reconstructor block){
        block.requirements = ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(6) + 1, 5, (int) (block.health / 2), 5, true);
        
        Consume[] save = block.consumers;
        block.consumers = new Consume[0];
        for(Consume consume : save) block.removeConsumer(consume);
        block.consumeItems(ResourceMapper.getRandomItemStacks(ResourceMapper.getRandomInt(5) + 1, 5, (int) (block.health / 2), 5, true));
    }
}
