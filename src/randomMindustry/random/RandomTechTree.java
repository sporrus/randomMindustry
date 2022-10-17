package randomMindustry.random;

import arc.struct.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import randomMindustry.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.block.blocks.*;
import randomMindustry.mappers.item.*;

import static mindustry.content.TechTree.*;

public class RandomTechTree {
    public static void load() {
        Main.random.techTree = nodeRoot("rm-random", (RandomCore)BlockMapper.generatedBlocks.find(b -> b instanceof RandomCore c && c.id == 0), () -> {
            ItemMapper.generatedItems.each(item -> {
                CustomItem depend = depends(item);
                item.techNode = new TechNode(
                        depend == null ? TechTree.context() : depend.techNode,
                        item,
                        item.researchRequirements()
                );
                item.techNode.objectives.add(new Objectives.Produce(item));
            });

            BlockMapper.generatedBlocks.copy().sort((a, b) -> a.getTier() - b.getTier()).each(RandomBlock::generateNode);
        });
    }

    // TODO: move to CustomItem
    public static CustomItem depends(CustomItem item) {
        if (item.tierType == ItemTierType.drill) {
            if (item.hardness == 1) return null;
            RandomDrill drill = (RandomDrill) BlockMapper.generatedBlocks
                    .select(b -> b instanceof RandomDrill)
                    .select(d -> ((RandomDrill) d).tier >= item.hardness)
                    .sort((a, b) -> ((RandomDrill) a).tier - ((RandomDrill) b).tier)
                    .get(0);
            return (CustomItem) Seq.with(drill.requirements).sort((a, b) -> b.amount - a.amount).get(0).item;
        } else if (item.tierType == ItemTierType.craft) {
            RandomCrafter crafter = (RandomCrafter) BlockMapper.generatedBlocks
                    .select(b -> b instanceof RandomCrafter)
                    .select(c -> Seq.with(((RandomCrafter) c).outputItems).contains(i -> i.item == item))
                    .get(0);
            return (CustomItem) getInputs(crafter).sort((a, b) -> b.amount - a.amount).get(0).item;
        }
        return null;
    }

    public static Seq<ItemStack> getInputs(Block block) {
        Seq<ItemStack> items = new Seq<>();
        block.init(); // TODO this is very bad
        for (Consume consume : block.consumers) {
            if (consume instanceof ConsumeItems i) items.addAll(i.items);
        }
        return items;
    }
}
