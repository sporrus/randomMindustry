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
    private static RandomDrill lastDrill;
    private static RandomCrafter lastCraft;
    private static RandomConveyor lastConv;
    private static RandomItemTurret lastItur;
    private static RandomWall lastWall;
    
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

            // TODO: this should be moved into RandomBlock classes (prob getTechParent method)
            Seq<RandomBlock> blocks = BlockMapper.generatedBlocks.sort((a, b) -> a.getTier() - b.getTier());
            RandomDrill firstDrill = (RandomDrill) blocks.select(b -> b instanceof RandomDrill).get(0);
            Seq<RandomBlock> convSeq = blocks.select(b -> b instanceof RandomConveyor);
            Seq<RandomBlock> routSeq = blocks.select(b -> b instanceof RandomRouter);
            Seq<RandomBlock> iturSeq = blocks.select(b -> b instanceof RandomItemTurret);
            blocks.each(block -> {
                if (block instanceof RandomDrill drill) {
                    drill.techNode = new TechNode(
                            lastDrill == null ? TechTree.context() : lastDrill.techNode,
                            drill,
                            drill.researchRequirements()
                    );
                    lastDrill = drill;
                } else if (block instanceof RandomCrafter craft) {
                    craft.techNode = new TechNode(
                            lastCraft == null ? firstDrill.techNode : lastCraft.techNode,
                            craft,
                            craft.researchRequirements()
                    );
                    lastCraft = craft;
                } else if (block instanceof RandomConveyor conv) {
                    conv.techNode = new TechNode(
                            lastConv == null ? TechTree.context() : lastConv.techNode,
                            conv,
                            conv.requirements // intended
                    );
                    lastConv = conv;
                } else if (block instanceof RandomRouter rout) {
                    rout.techNode = new TechNode(
                            ((RandomConveyor)convSeq.get(rout.id)).techNode,
                            rout,
                            rout.requirements // also intended
                    );
                } else if (block instanceof RandomItemBridge ibrid) {
                    ibrid.techNode = new TechNode(
                            ((RandomRouter)routSeq.get(ibrid.id)).techNode,
                            ibrid,
                            ibrid.requirements // also also intended
                    );
                } else if (block instanceof RandomItemTurret itur) {
                    itur.techNode = new TechNode(
                            lastItur == null ? TechTree.context() : lastItur.techNode,
                            itur,
                            itur.researchRequirements()
                    );
                    lastItur = itur;
                } else if (block instanceof RandomWall wall) {
                    wall.techNode = new TechNode(
                            lastWall == null ? ((RandomItemTurret)iturSeq.get(0)).techNode : lastWall.techNode,
                            wall,
                            wall.researchRequirements()
                    );
                    lastWall = wall;
                }
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
