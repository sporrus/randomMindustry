package randomMindustry;

import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.content.Items;
import mindustry.type.Item;
import mindustry.type.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceMapper {
    public static String[] tags = new String[]{"hand", "drill", "craft"};
    public static ObjectMap<String, ItemPack> itemMap = new ObjectMap<>();

    // TODO: make randomized items
    public static void init() {
        itemMap.put("hand0", new ItemPack("hand", 0,
                Items.copper, Items.lead, Items.scrap
        ));
        itemMap.put("drill0", new ItemPack("drill", 0,
                Items.sand, Items.coal
        ));
        itemMap.put("drill1", new ItemPack("drill", 1,
                Items.titanium
        ));
        itemMap.put("drill2", new ItemPack("drill", 2,
                Items.thorium
        ));
        itemMap.put("craft0", new ItemPack("craft", 0,
                Items.metaglass, Items.graphite, Items.silicon, Items.sporePod, Items.pyratite
        ));
        itemMap.put("craft1", new ItemPack("craft", 1,
                Items.plastanium, Items.phaseFabric, Items.surgeAlloy, Items.sporePod, Items.blastCompound
        ));
    }

    public static Item getAnyItem() {
        return randomSeq(randomSeq(itemMap.values().toSeq()).all);
    }

    public static ItemStack[] getRandomStack(int maxTier, int maxItemCount, int maxItemsInStack, boolean locked) {
        ItemStack[] stacks = new ItemStack[getRandomInt(maxItemCount) + 1];
        for (int i = 0; i < stacks.length; i++) {
            stacks[0] = new ItemStack(
                    getItemFromTier(getRandomInt(maxTier), locked),
                    getRandomInt(maxItemsInStack)
            );
        }
        return stacks;
    }

    public static Item getItemFromTier(int tier, boolean locked) {
        int tagIndex = 0;
        Item item = getItemFromPack(itemMap.get(tags[tagIndex] + tier), locked);
        while (item == null) {
            tagIndex++;
            if (!itemMap.containsKey(tags[tagIndex] + tier)) return null;
            item = getItemFromPack(itemMap.get(tags[tagIndex] + tier), locked);
        }
        return item;
    }

    public static Item getCraftItem() {
        return getItemFromTag("craft", true);
    }

    public static Item getItemFromTag(String tag, boolean locked) {
        int tier = 0;
        Item item = getItemFromPack(itemMap.get(tag + tier), locked);
        while (item == null) {
            tier++;
            if (!itemMap.containsKey(tag + tier)) return null;
            item = getItemFromPack(itemMap.get(tag + tier), locked);
        }
        return item;
    }

    public static Item getItemFromPack(ItemPack pack, boolean locked) {
        Seq<Item> seq = (locked ? pack.locked : pack.all);
        if (seq.size <= 0) return null;
        int i = getRandomInt(seq.size);
        if (locked) return seq.remove(i);
        return seq.get(i);
    }

    public static <T> T randomSeq(Seq<T> seq) {
        return seq.get(getRandomInt(seq.size));
    }

    public static int getRandomInt(int max) {
        return (int) (Math.random() * max);
    }
}
