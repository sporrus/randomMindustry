package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.content.*;
import mindustry.entities.bullet.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import randomMindustry.random.util.*;
import randomMindustry.util.*;
import randomMindustry.util.techTrees.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class BlockMapper {
    public static void init() {
        Seq<Block> blocks = content.blocks().copy();
        RandomUtil.shuffle(blocks);
        blocks.each(BlockMapper::modify);
    }

    public static void modify(Block block) {
        Seq<Category> cats = new Seq<>();
        cats.addAll(Category.all);
        if (settings.getBool("rmchaos-category-rand", false)) block.category = cats.random(RandomUtil.getRand());
        if (!TechUtil.getRoot(block).contains(Planets.serpulo)) return;
        if (block instanceof GenericCrafter gencrafter) {
            modifyCrafter(gencrafter);
        } else if (block instanceof Drill drill) {
            modifyDrill(drill);
        } else if (block instanceof Conveyor conveyor) {
            modifyConveyor(conveyor);
        } else if (block instanceof Wall wall) {
            modifyWall(wall);
        } else if (block instanceof Turret turret) {
            modifyTurret(turret);
        } else if (block instanceof UnitFactory ufactory) {
            modifyUnitFactory(ufactory);
        } else if (block instanceof Reconstructor recons) {
            modifyReconstructor(recons);
        } else if (block instanceof PowerGenerator generator) {
            modifyGenerator(generator);
        } else {
            modifyBlock(block);
        }
        
        block.init();
    }

    public static void modifyGenerator(PowerGenerator block) {
        if (block instanceof ConsumeGenerator) {
            Util.removeAllConsumers(block);
            Seq<Item> consumes = content.items().select((l) -> TechUtil.getRoot(l).contains(Planets.serpulo));
            Seq<Liquid> liquids = content.liquids().select((l) -> TechUtil.getRoot(l).contains(Planets.serpulo));
            if (RandomUtil.getRand().chance(0.25)) {
                block.consumeLiquid(liquids.random(RandomUtil.getRand()), RandomUtil.getRand().random(1f));
            }
            Seq<Item> selectedConsumers = new Seq<>();
            int consumers = RandomUtil.getRand().random(1,3);
            for (int i = 0; i < consumers; i++) {
                Item consume = consumes.random();
                selectedConsumers.add(consume);
                consumes.remove(consume);
            }
            ConsumeItemFilter consumeItemFilter = new ConsumeItemFilter(selectedConsumers::contains);
            block.consume(consumeItemFilter);
        }
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
    }

    public static void modifyTurret(Turret block) {
        block.shootSound = Sounds.getSound(RandomUtil.getRand().random(0, 71));
        block.loopSound = Sounds.getSound(RandomUtil.getRand().random(0, 71));
        block.reload = RandomUtil.getRand().random(300f);
        block.stats = new Stats();
        block.stats.intialized = true;
        if (block instanceof ItemTurret turret) {
            Seq<Object> ammo = new Seq<>();
            Seq<Item> items = content.items().select((item -> TechUtil.getRoot(item).contains(Planets.serpulo)));
            Seq<BulletType> bullets = content.bullets();
            int count = RandomUtil.getRand().random(1, 5);
            for (int i = 0; i < count; i++) {
                ammo.add(items.random(RandomUtil.getRand()), bullets.random(RandomUtil.getRand()));
            }
            Seq<Consume> nonOptionalConsumers = new Seq<>(block.nonOptionalConsumers);
            Util.removeConsumers(block, nonOptionalConsumers::contains);
            turret.ammo(ammo.toArray());
        } else if (block instanceof LiquidTurret turret) {
            Seq<Object> ammo = new Seq<>();
            Seq<Liquid> liquids = content.liquids().select((liquid -> TechUtil.getRoot(liquid).contains(Planets.serpulo)));
            Seq<BulletType> bullets = content.bullets();
            int count = RandomUtil.getRand().random(1, 5);
            for (int i = 0; i < count; i++) ammo.add(liquids.random(RandomUtil.getRand()), bullets.random(RandomUtil.getRand()));
            Seq<Consume> nonOptionalConsumers = new Seq<>(block.nonOptionalConsumers);
            Util.removeConsumers(block, nonOptionalConsumers::contains);
            turret.ammo(ammo.toArray());
        } else if (block instanceof LaserTurret turret) {
            turret.consumePower(RandomUtil.getRand().random(20000) / 1000f);
            turret.shootType = content.bullets().random(RandomUtil.getRand());
        }
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5,(int) Math.floor(block.health / 2d), 5, true);
        block.setStats();
    }

    public static void modifyCrafter(GenericCrafter block) {
        Item item = ResourceMapper.getGenericCrafterOut();
        if (item == null) item = ResourceMapper.getRandomItem(false);
        int count = RandomUtil.getRand().random(10) + 1;
        block.outputItems = new ItemStack[]{new ItemStack(item, count)};
        Util.removeAllConsumers(block);
        int tier = ResourceMapper.getTierOfItem(item);
        block.consumeItems(ResourceMapper.getRandomItemStacks(tier, 3, 10, 1, true));
        block.craftTime = RandomUtil.getRand().random(300f);
        block.requirements = ResourceMapper.getRandomItemStacks(tier, 5, (int) Math.floor(block.health / 2d), 5, true);
    }

    public static void modifyConveyor(Conveyor block) {
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 3, (int) Math.floor(block.health / 4d), 1, true);
    }

    public static void modifyBlock(Block block) {
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
    }

    public static void modifyDrill(Drill block) {
        int localTier = block.tier * 2 - 3;
        block.requirements = ResourceMapper.getRandomItemStacks(localTier, 5, (int) Math.floor(block.health / 4d), 5, true);
    }

    public static void modifyWall(Wall block) {
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, block.size * 10, 5, true);
    }
    
    public static void modifyUnitFactory(UnitFactory block){
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
        
        Seq<UnitFactory.UnitPlan> plans = block.plans;
        
        plans.each(plan -> {
            // randomize plan build time and unit?
            plan.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(3) + 1, 5, (int) Math.floor(plan.unit.health / 2d), 5, true);
        });
    }
    
    public static void modifyReconstructor(Reconstructor block){
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
        Util.removeAllConsumers(block);
        block.consumeItems(ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(5) + 1, 5, (int) Math.floor(block.health / 2d), 5, true));
    }
}
