package randomMindustry.random.mappers.blocks;

import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;
import mindustry.world.consumers.*;

import randomMindustry.random.mappers.*;
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
        Util.resetStats(block);
        if (block instanceof GenericCrafter gencrafter) {
            modifyCrafter(gencrafter);
        } else if (block instanceof Drill drill) {
            modifyDrill(drill);
        } else if (block instanceof Conveyor conveyor) {
            modifyConveyor(conveyor);
        } else if (block instanceof Wall wall) {
            modifyWall(wall);
        } else if (block instanceof Turret turret) {
            TurretMapper.map(turret);
        } else if (block instanceof UnitFactory ufactory) {
            modifyUnitFactory(ufactory);
        } else if (block instanceof Reconstructor recons) {
            modifyReconstructor(recons);
        } else if (block instanceof PowerGenerator generator) {
            GeneratorMapper.map(generator);
        } else if (block instanceof CoreBlock core) {
            modifyCore(core);
        } else {
            modifyBlock(block);
        }
        Util.updateStats(block);
    }

    public static void modifyCore(CoreBlock block) {
        block.unitType.weapons.each((w) -> w.bullet.buildingDamageMultiplier = 0.01f);
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(3, 6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
    }

    public static void modifyCrafter(GenericCrafter block) {
        Item item = ResourceMapper.getGenericCrafterOut();
        if (item == null) item = ResourceMapper.getRandomItem(false);
        int count = RandomUtil.getRand().random(1, 10);
        block.outputItems = new ItemStack[]{new ItemStack(item, count)};
        Util.removeAllConsumers(block);
        int tier = ResourceMapper.getTierOfItem(item);
        if (tier >= GeneratorMapper.getLowestPowerTier() && RandomUtil.getRand().chance(0.25)) {
            block.consumePower(RandomUtil.getRand().random(20f));
        }
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

    public static void modifyUnitFactory(UnitFactory block) {
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);

        Seq<UnitFactory.UnitPlan> plans = block.plans;

        plans.each(plan -> {
            // randomize plan build time and unit?
            plan.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(3) + 1, 5, (int) Math.floor(plan.unit.health / 2d), 5, true);
        });
    }

    public static void modifyReconstructor(Reconstructor block) {
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
        Util.removeAllConsumers(block);
        block.consumeItems(ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(5) + 1, 5, (int) Math.floor(block.health / 2d), 5, true));
    }
}
