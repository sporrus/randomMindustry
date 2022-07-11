package randomMindustry.random.mappers.blocks;

import arc.struct.Seq;
import mindustry.content.Planets;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.world.blocks.power.ConsumeGenerator;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.consumers.ConsumeItemFilter;
import randomMindustry.random.mappers.ResourceMapper;
import randomMindustry.random.util.RandomUtil;
import randomMindustry.util.Util;
import randomMindustry.util.techTrees.TechUtil;

import static mindustry.Vars.content;

public class GeneratorMapper {
    private static int lowestTier = ResourceMapper.getMaxTier();

    public static void map(PowerGenerator block) {
        if (block instanceof ConsumeGenerator) {
            boolean hard = false;

            Util.removeAllConsumers(block);
            Seq<Item> consumes = content.items().select((l) -> TechUtil.getRoot(l).contains(Planets.serpulo));
            Seq<Liquid> liquids = content.liquids().select((l) -> TechUtil.getRoot(l).contains(Planets.serpulo));
            if (RandomUtil.getRand().chance(0.25)) {
                hard = true;
                block.consumeLiquid(liquids.random(RandomUtil.getRand()), RandomUtil.getRand().random(0.25f));
            }
            int tier = ResourceMapper.getMaxTier();
            Seq<Item> selectedConsumers = new Seq<>();
            int consumers = RandomUtil.getRand().random(1, 3);
            for (int i = 0; i < consumers; i++) {
                Item consume = consumes.random(RandomUtil.getRand());
                selectedConsumers.add(consume);
                consumes.remove(consume);
                tier = Math.min(tier, ResourceMapper.getTierOfItem(consume));
            }
            if (hard) tier += 3;
            lowestTier = Math.min(lowestTier, tier);
            block.powerProduction = RandomUtil.getRand().random(100f);
            ConsumeItemFilter consumeItemFilter = new ConsumeItemFilter(selectedConsumers::contains);
            block.consume(consumeItemFilter);
        }
        block.requirements = ResourceMapper.getRandomItemStacks(RandomUtil.getRand().random(6) + 1, 5, (int) Math.floor(block.health / 2d), 5, true);
    }

    public static int getLowestPowerTier() {
        return lowestTier;
    }
}
