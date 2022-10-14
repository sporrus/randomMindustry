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

import java.util.*;

import static mindustry.content.TechTree.*;

public class RandomTechTree {
    public static void load() {
        Main.random.techTree = nodeRoot("rm-random", Items.carbide, () -> {
            CustomItemSeq items = ItemMapper.generatedItems.copy();
            items.each(item -> {
                CustomItem depend = depends(item);
                item.techNode = new TechTree.TechNode(
                        depend == null ? TechTree.context() : depend.techNode,
                        item,
                        item.researchRequirements()
                );
                item.techNode.objectives.add(new Objectives.Produce(item));
            });
        });
    }

    public static CustomItem depends(CustomItem item) {
        if (item.hardness == 1) return null;
        CustomItem[] depend = {null};
        int[] amount = {0};
        int[] lastTier = {Integer.MAX_VALUE};
        BlockMapper.generatedBlocks.each(block -> {
            if (block instanceof RandomCrafter r) {
                if (!new Seq<>(r.outputItems).contains(i -> i.item == item)) return;
                ItemStack consume = getInputs(r).sort((a, b) -> b.amount - a.amount).get(0);
                if (amount[0] > consume.amount) return;
                depend[0] = (CustomItem) consume.item;
                amount[0] = consume.amount;
            } else if (block instanceof RandomDrill r) {
                if (r.tier < item.hardness - 1) return;
                if (r.tier > lastTier[0]) return;
                Item i = new Seq<>(r.requirements).sort((a, b) -> b.amount - a.amount).get(0).item;
                depend[0] = (CustomItem) i;
                lastTier[0] = r.tier;
            }
        });
        return depend[0];
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
