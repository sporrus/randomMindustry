package randomMindustry.random.mappers.blocks;

import arc.math.Mathf;
import arc.struct.*;
import arc.util.Log;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.blocks.heat.HeatProducer;
import mindustry.world.blocks.power.*;
import mindustry.world.blocks.production.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.blocks.units.*;

import mindustry.world.meta.BuildVisibility;
import randomMindustry.random.mappers.*;
import randomMindustry.random.util.*;
import randomMindustry.util.*;
import randomMindustry.util.techTrees.*;

import java.util.Arrays;
import java.util.Comparator;

import static mindustry.Vars.*;
import static arc.Core.*;

public class BlockMapper {
    private static Planet planet = Planets.serpulo;
    private static Seq<Block> selectedBlocks = new Seq<>();
    public static final Seq<String> genericCrafterTags = new Seq<>(new String[]{"press", "cultivator", "mixer", "smelter", "compressor", "weaver", "kiln", "pulverizer", "centrifuge", "condenser", "melter"});

    public static void init() {
        if (planet == null) selectedBlocks = content.blocks().copy();
        else selectedBlocks = content.blocks().select((b) -> TechUtil.getRoot(b).contains(planet));
        Seq<Block> unblocks = content.blocks().copy();
        unblocks.removeAll(selectedBlocks);
        unblocks.each(b -> {if (b.buildVisibility == BuildVisibility.shown) b.buildVisibility = BuildVisibility.debugOnly;});
        selectedBlocks.each(b -> {if (b.buildVisibility == BuildVisibility.debugOnly) b.buildVisibility = BuildVisibility.shown;});
        RandomUtil.shuffle(selectedBlocks);
        selectedBlocks.sort((a, b) -> {
            if (a instanceof PowerGenerator) return -1;
            else if (b instanceof PowerGenerator) return 1;
            return 0;
        });
        selectedBlocks.each(BlockMapper::modify);
    }

    public static Planet getCurrentPlanet() {
        return planet;
    }

    public static void setCurrentPlanet(Planet planet) {
        BlockMapper.planet = planet;
    }

    public static Seq<Block> getSelectedBlocks() {
        return selectedBlocks;
    }

    public static void modify(Block block) {
        Seq<Category> cats = new Seq<>();
        cats.addAll(Category.all);
        if (settings.getBool("rmchaos-category-rand", false)) block.category = cats.random(RandomUtil.getRand());
        if (block.buildVisibility != BuildVisibility.shown) return;
        Util.resetStats(block);
        if (block instanceof HeatProducer heatProducer) {
            modifyHeater(heatProducer);
        } else if (block instanceof GenericCrafter gencrafter) {
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
        } else if (block instanceof UnitAssembler assembler) {
            modifyUnitAssembler(assembler);
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
        block.localizedName = StringGenerator.generateCoreName();
        block.unitType.weapons.each((w) -> w.bullet.buildingDamageMultiplier = 0.01f);
        block.requirements = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(3, ItemMapper.maxTier) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
    }

    public static void modifyHeater(HeatProducer block) {
        modifyBlock(block);
    }

    public static void modifyCrafter(GenericCrafter block) {
        Item item = ItemMapper.getGenericCrafterOut();
        if (item == null) item = ItemMapper.getRandomItem(false);
        int count = RandomUtil.getRand().random(1, 10);
        block.outputItems = new ItemStack[]{new ItemStack(item, count)};
        Util.removeAllConsumers(block);
        Util.removeBars(block);
        int tier = ItemMapper.getTierOfItem(item)-1;
        if (tier >= GeneratorMapper.getLowestPowerTier() && RandomUtil.getRand().chance(0.25)) {
            block.hasPower = true;
            block.consumePower(Mathf.round(RandomUtil.getRand().random(20f), 1/15f));
        } else {
            block.hasPower = false;
        }
        block.hasLiquids = false;
        ItemStack[] itemStacks = ItemMapper.getRandomItemStacks(tier, 3, 10, 1, true);
        if (tier > -1) block.consumeItems(itemStacks);
        else block.consumeItems();
        block.craftTime = Mathf.round(RandomUtil.getRand().random(20f, 300f), 15f);
        block.requirements = ItemMapper.getRandomItemStacks(tier, 5, (int) Math.floor(block.health / 2d), 5, true);

        if (!headless) {
            String factory = (tier == -1 ? "creator" : genericCrafterTags.random(RandomUtil.getClientRand()));

            StringBuilder from = new StringBuilder();
            if (itemStacks.length > 0) from.append(itemStacks[0].item.localizedName.toLowerCase()).append("[lightgray]");
            if (itemStacks.length > 1) {
                for (int i = 1; i < itemStacks.length - 1; i++) {
                    from.append(", ").append(itemStacks[i].item.localizedName.toLowerCase()).append("[lightgray]");
                }
                from.append(" and ").append(itemStacks[itemStacks.length - 1].item.localizedName.toLowerCase()).append("[lightgray]");
            }
            String to = item.localizedName.toLowerCase() + "[lightgray]";

            block.localizedName = bundle.format("crafter.rm-name." + factory, item.localizedName);
            block.description = bundle.format("crafter.rm-description." + factory, from.toString(), to);
        }
    }

    public static void modifyConveyor(Conveyor block) {
        block.requirements = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(ItemMapper.maxTier) + 1, 3, (int) Math.floor(block.health / 4d), 1, true);
    }

    public static void modifyBlock(Block block) {
        block.requirements = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(ItemMapper.maxTier) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
    }

    public static void modifyDrill(Drill block) {
        block.localizedName = StringGenerator.generateDrillName();
        int localTier = block.tier - 2;
        ItemMapper.ItemPack drillPack = ItemMapper.getPackByTagAndLocalTier("drill", localTier);
        int globalTier = ItemMapper.maxTier;
        if (drillPack != null) globalTier = drillPack.tier - 1;
        Log.info(block.localizedName + " - " + globalTier);
        block.requirements = ItemMapper.getRandomItemStacks(globalTier, 5, (int) Math.floor(block.health / 4d), 5, true);
    }

    public static void modifyWall(Wall block) {
        ItemStack[] items = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(ItemMapper.maxTier) + 1, 5, block.size * 10, 1, true);
        block.requirements = Arrays.copyOf(items, items.length);
        Arrays.sort(items, (a, b) -> { return b.amount - a.amount; });
        ItemStack mainItem = items[0];
        String size = "";
        if (block.size == 2) size = "Large ";
        else if (block.size == 3) size = "Huge ";
        else if (block.size == 4) size = "Gigantic ";
        block.localizedName = size + mainItem.item.localizedName + " Wall";
    }

    public static void modifyUnitFactory(UnitFactory block) {
        block.localizedName = StringGenerator.generateUnitFactoryName();
        
        block.requirements = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(ItemMapper.maxTier) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);

        block.plans.each(plan -> {
            plan.time = (plan.unit.health / 1.25f) * RandomUtil.getRand().random(0.5f, 1.5f);
            plan.requirements = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(3) + 1, 5, (int) Math.floor(plan.unit.health / 2d), 5, true);
        });
    }

    public static void modifyReconstructor(Reconstructor block) {
        block.localizedName = StringGenerator.generateReconstructorName();
        block.requirements = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(ItemMapper.maxTier) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
        Util.removeAllConsumers(block);
        block.consumeItems(ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(ItemMapper.maxTier) + 1, 5, (int) Math.floor(block.health / 2d), 5, true));
        
        block.constructTime = 0f;
        block.upgrades.each(upgrade -> block.constructTime += ((upgrade[1].health / 0.25f) * RandomUtil.getRand().random(0.5f, 1.5f)));
        block.constructTime /= block.upgrades.size;
    }
    
    public static void modifyUnitAssembler(UnitAssembler block) {
        block.localizedName = StringGenerator.generateUnitFactoryName();
        
        block.requirements = ItemMapper.getRandomItemStacks(RandomUtil.getRand().random(ItemMapper.maxTier) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
        
        block.dronesCreated = RandomUtil.getRand().random(1, 8);
        block.droneConstructTime = (block.droneType.health / 1.25f) * RandomUtil.getRand().random(0.5f, 1.5f);
        
        block.plans.each(plan -> {
            plan.time = (plan.unit.health / 1.25f) * RandomUtil.getRand().random(0.5f, 1.5f);
            // TODO: payload stack generator
        });
    }
}
