package randomMindustry.random.mappers.blocks;

import arc.struct.Seq;
import mindustry.content.Liquids;
import mindustry.content.Planets;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.blocks.power.*;
import mindustry.world.consumers.ConsumeItemFilter;
import mindustry.world.meta.Attribute;
import randomMindustry.random.mappers.ItemMapper;
import randomMindustry.random.util.RandomUtil;
import randomMindustry.util.Util;
import randomMindustry.util.techTrees.TechUtil;

import static mindustry.Vars.content;

public class GeneratorMapper {
    private static int lowestTier = ItemMapper.getMaxTier();

    public static void map(PowerGenerator block) {
        if (block instanceof ConsumeGenerator cg) {
            boolean hard = false;
            Util.removeAllConsumers(cg);
            Util.removeBars(cg);
            Seq<Item> consumes = ItemMapper.getSelectedItems().copy();
            Seq<Liquid> liquids = content.liquids().select((l) -> TechUtil.getRoot(l).contains(Planets.serpulo));
            if (RandomUtil.getRand().chance(0.25)) {
                hard = true;
                cg.consumeLiquid(liquids.random(RandomUtil.getRand()), RandomUtil.getRand().random(0.25f));
            }
            int tier = ItemMapper.getMaxTier();
            Seq<Item> selectedConsumers = new Seq<>();
            int consumers = RandomUtil.getRand().random(1, 3);
            for (int i = 0; i < consumers; i++) {
                Item consume = consumes.random(RandomUtil.getRand());
                selectedConsumers.add(consume);
                consumes.remove(consume);
                tier = Math.min(tier, ItemMapper.getTierOfItem(consume));
            }
            if (hard) tier += 3;
            lowestTier = Math.min(lowestTier, tier);
            cg.powerProduction = RandomUtil.getRand().random(100f);
            ConsumeItemFilter consumeItemFilter = new ConsumeItemFilter(selectedConsumers::contains);
            cg.consume(consumeItemFilter);
            cg.requirements = ItemMapper.getRandomItemStacks(tier, 5, (int) Math.floor(cg.health / 2d), 5, true);
        } else if (block instanceof NuclearReactor nr) {
            Util.removeAllConsumers(nr);
            Util.removeBars(nr);
            Seq<Item> items = ItemMapper.getSelectedItems().copy();
            Seq<Liquid> liquids = content.liquids().select((l) -> TechUtil.getRoot(l).contains(Planets.serpulo));
            Item item = items.random(RandomUtil.getRand());
            int tier = ItemMapper.getTierOfItem(item);
            lowestTier = Math.min(tier, lowestTier);
            Liquid liquid = liquids.random(RandomUtil.getRand());
            nr.powerProduction = RandomUtil.getRand().random(100f);
            nr.fuelItem = item;
            nr.consumeItem(item);
            nr.consumeLiquid(liquid, nr.heating / nr.coolantPower).update(false);
            nr.requirements = ItemMapper.getRandomItemStacks(tier, 5, (int) Math.floor(nr.health / 2d), 5, true);
        } else if (block instanceof ImpactReactor ir) {
            Util.removeAllConsumers(ir);
            Util.removeBars(ir);
            Seq<Item> items = ItemMapper.getSelectedItems().copy();
            Seq<Liquid> liquids = content.liquids().select((l) -> TechUtil.getRoot(l).contains(Planets.serpulo));
            Item item = items.random(RandomUtil.getRand());
            int tier = ItemMapper.getTierOfItem(item);
            lowestTier = Math.min(tier, lowestTier);
            Liquid liquid = liquids.random(RandomUtil.getRand());
            ir.powerProduction = RandomUtil.getRand().random(100f);
            ir.consumeItem(item);
            ir.consumeLiquid(liquid, RandomUtil.getRand().random(1f));
            ir.consumePower(RandomUtil.getRand().random(Math.max(0, ir.powerProduction - 1)));
            ir.requirements = ItemMapper.getRandomItemStacks(tier, 5, (int) Math.floor(ir.health / 2d), 5, true);
        } else if (block instanceof SolarGenerator sg) {
            int tier = RandomUtil.getRand().random(1, ItemMapper.maxTier);
            lowestTier = Math.min(tier, lowestTier);
            sg.powerProduction = tier * sg.size / 15f;
            sg.requirements = ItemMapper.getRandomItemStacks(tier, 5, (int) Math.floor(sg.health / 2d), 5, true);
        } else if (block instanceof ThermalGenerator tg) {
            int tier = RandomUtil.getRand().random(1, ItemMapper.maxTier);
            lowestTier = Math.min(tier, lowestTier);
            tg.powerProduction = tier * tg.size * 100f;
            tg.requirements = ItemMapper.getRandomItemStacks(tier, 5, (int) Math.floor(tg.health / 2d), 5, true);
        }
    }

    public static int getLowestPowerTier() {
        return lowestTier;
    }
}
