package randomMindustry.random;

import arc.struct.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import randomMindustry.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.block.blocks.*;
import randomMindustry.mappers.item.*;

import java.util.*;

import static mindustry.content.TechTree.*;

public class RandomTechTree {
    private static RandomDrill lastDrill;
    private static RandomConveyor lastConv;
    // private static RandomRouter lastRout;
    
    public static void load() {
        Main.random.techTree = nodeRoot("rm-random", Items.carbide, () -> {
            ItemMapper.generatedItems.each(item -> {
                CustomItem depend = depends(item);
                item.techNode = new TechNode(
                        depend == null ? TechTree.context() : depend.techNode,
                        item,
                        item.researchRequirements()
                );
                item.techNode.objectives.add(new Objectives.Produce(item));
            });
            
            Seq<RandomBlock> drillSeq = BlockMapper.generatedBlocks.select(block -> block instanceof RandomDrill).sort((a, b) -> ((RandomDrill)a).tier - ((RandomDrill)b).tier);
            drillSeq.each(block -> {
                RandomDrill drill = (RandomDrill)block;
                drill.techNode = new TechNode(
                    lastDrill == null ? TechTree.context() : lastDrill.techNode,
                    drill,
                    drill.researchRequirements()
                );
                lastDrill = drill;
            });
            
            Seq<RandomBlock> convSeq = BlockMapper.generatedBlocks.select(block -> block instanceof RandomConveyor).sort((a, b) -> ((RandomConveyor)a).tier - ((RandomConveyor)b).tier);
            convSeq.each(block -> {
                RandomConveyor conv = (RandomConveyor)block;
                conv.techNode = new TechNode(
                    lastConv == null ? TechTree.context() : lastConv.techNode,
                    conv,
                    conv.requirements // intended
                );
                lastConv = conv;
            });
            
            Seq<RandomBlock> routSeq = BlockMapper.generatedBlocks.select(block -> block instanceof RandomRouter).sort((a, b) -> ((RandomRouter)a).tier - ((RandomRouter)b).tier);
            routSeq.each(block -> {
                RandomRouter rout = (RandomRouter)block;
                rout.techNode = new TechNode(
                    convSeq.get(rout.id).techNode, // lastRout == null ? convSeq.get(0).techNode : lastRout.techNode,
                    rout,
                    rout.requirements // also intended
                );
                // lastRout = rout;
            });
        });
    }

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
