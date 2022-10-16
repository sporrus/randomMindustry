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
    private static final Seq<RandomDrill> lastDrill = new Seq<>();
    private static final Seq<RandomCrafter> lastCraft = new Seq<>();
    private static final Seq<RandomConveyor> lastConv = new Seq<>();
    private static final Seq<RandomItemTurret> lastItur = new Seq<>();
    private static final Seq<RandomWall> lastWall = new Seq<>();
    private static SyncedRand r = new SyncedRand();
    
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
            Seq<RandomBlock> convSeq = blocks.select(b -> b instanceof RandomConveyor);
            Seq<RandomBlock> routSeq = blocks.select(b -> b instanceof RandomRouter);
            Seq<RandomBlock> iturSeq = blocks.select(b -> b instanceof RandomItemTurret);
            blocks.each(block -> {
                if (block instanceof RandomDrill drill) {
                    drill.techNode = new TechNode(
                            lastDrill.size == 0 ? TechTree.context() : lastDrill.random(r).techNode,
                            drill,
                            drill.researchRequirements()
                    );
                    if (r.chance(0.5) && lastCraft.size > 0) lastCraft.remove(0);
                    lastDrill.add(drill);
                } else if (block instanceof RandomCrafter craft) {
                    craft.techNode = new TechNode(
                            lastCraft.size == 0 ? TechTree.context() : lastCraft.random(r).techNode,
                            craft,
                            craft.researchRequirements()
                    );
                    if (r.chance(0.5) && lastCraft.size > 0) lastCraft.remove(0);
                    lastCraft.add(craft);
                } else if (block instanceof RandomConveyor conv) {
                    conv.techNode = new TechNode(
                            lastConv.size == 0 ? TechTree.context() : lastConv.random(r).techNode,
                            conv,
                            conv.researchRequirements() // intended
                    );
                    if (r.chance(0.5) && lastConv.size > 0) lastConv.remove(0);
                    lastConv.add(conv);
                } else if (block instanceof RandomRouter rout) {
                    rout.techNode = new TechNode(
                            ((RandomConveyor)convSeq.get(rout.id)).techNode,
                            rout,
                            rout.researchRequirements() // also intended
                    );
                } else if (block instanceof RandomItemBridge ibrid) {
                    ibrid.techNode = new TechNode(
                            ((RandomRouter)routSeq.get(ibrid.id)).techNode,
                            ibrid,
                            ibrid.researchRequirements() // also also intended
                    );
                } else if (block instanceof RandomItemTurret itur) {
                    itur.techNode = new TechNode(
                            lastItur.size == 0 ? TechTree.context() : lastItur.random(r).techNode,
                            itur,
                            itur.researchRequirements()
                    );
                    if (r.chance(0.5) && lastItur.size > 0) lastItur.remove(0);
                    lastItur.add(itur);
                } else if (block instanceof RandomWall wall) {
                    wall.techNode = new TechNode(
                            lastWall.size == 0 ? ((RandomItemTurret)iturSeq.get(0)).techNode : lastWall.random(r).techNode,
                            wall,
                            wall.researchRequirements()
                    );
                    if (r.chance(0.5) && lastWall.size > 0) lastWall.remove(0);
                    lastWall.add(wall);
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
