package randomMindustry;

import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;

public class ResourceMapper {
    public static Seq<String> tags = Seq.with("hand", "drill", "craft");
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

    public static ItemStack[] getRandomStack(int maxTier, int maxItemCount, int maxItemsInStack, boolean unique) {
        ObjectMap<String, ItemPack> temp = itemMap.copy();
        int count = getRandomInt(maxItemCount) + 1;
        Seq<ItemStack> stacks = new Seq<>();
        for (int i = 0; i < count; i++) {
            Item item = getItemFromTier(getRandomInt(maxTier), unique);
            if (item == null) continue;
            ItemStack stack = new ItemStack(item, getRandomInt(maxItemsInStack) + 1);
            stacks.add(stack);
        }
        itemMap = temp.copy();
        return stacks.toArray(ItemStack.class);
    }

    public static Item getItemFromTier(int tier, boolean locked) {
        boolean loop = false;
        int tagIndex = tags.indexOf(tags.random());
        Item item = getItemFromPack(itemMap.get(tags.get(tagIndex) + tier), locked);
        while (item == null) {
            tagIndex++;
            if (tagIndex >= tags.size || !itemMap.containsKey(tags.get(tagIndex) + tier)) {
                if (loop) return null;
                tagIndex = 0;
                loop = true;
            }
            item = getItemFromPack(itemMap.get(tags.get(tagIndex) + tier), locked);
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
        if (pack == null) return null;
        Seq<Item> seq = (locked ? pack.locked : pack.all);
        Item item = seq.random();
        if (locked) seq.remove(item);
        return item;
    }

    public static <T> T randomSeq(Seq<T> seq) {
        return seq.random();
    }

    public static int getRandomInt(int max) {
        return (int) (Math.random() * max);
    }
}
