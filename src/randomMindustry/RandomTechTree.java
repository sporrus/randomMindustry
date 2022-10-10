package randomMindustry;

import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.content.TechTree;
import mindustry.game.Objectives;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItems;
import randomMindustry.mappers.block.blocks.RandomCrafter;
import randomMindustry.mappers.item.CustomItem;
import randomMindustry.mappers.item.ItemMapper;

import java.util.Comparator;

import static mindustry.content.TechTree.nodeRoot;

public class RandomTechTree {
    public static void load() {
        Main.random.techTree = nodeRoot("rm-random", Items.carbide, () -> {
            Seq<CustomItem> items = ItemMapper.combine(ItemMapper.packs).all;
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
        CustomItem[] depend = {null};
        Vars.content.blocks().each(b -> {
            if (!(b instanceof RandomCrafter r)) return;
            if (!new Seq<>(r.outputItems).contains(i -> i.item == item)) return;
            Seq<ItemStack> consume = getInputs(r).sort(Comparator.comparingInt(a -> a.amount));
            if (consume.size < 1) return;
            depend[0] = (CustomItem) consume.get(0).item;
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
