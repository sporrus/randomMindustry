package randomMindustry;

import arc.struct.Seq;
import mindustry.content.Blocks;
import mindustry.content.Items;
import randomMindustry.mappers.item.CustomItem;
import randomMindustry.mappers.item.ItemMapper;

import static mindustry.content.TechTree.*;

public class RandomTechTree {
    public static void load() {
        Main.random.techTree = nodeRoot("rm-random", Items.carbide, () -> {
            addItems(ItemMapper.generatedItems);
        });
    }

    public static void addItems(Seq<CustomItem> items) {
        if (items.size < 1) return;
        CustomItem item = items.get(0);
        items.remove(item);
        node(item, () -> addItems(items));
    }
}
