package randomMindustry;

import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;

public class ResourceMapper {
    public static Seq<String> tags = Seq.with("hand", "drill", "craft");
    public static Seq<ItemPack> itemMap = new Seq<>();

    // TODO: make randomized items
    public static void init() {
        itemMap = new Seq<>();
        itemMap.add(new ItemPack("hand", 0, 0,
                Items.copper, Items.lead, Items.scrap
        ));
        itemMap.add(new ItemPack("drill", 1, 0,
                Items.sand, Items.coal
        ));
        itemMap.add(new ItemPack("craft", 2, 0,
                Items.metaglass, Items.graphite, Items.silicon, Items.sporePod, Items.pyratite
        ));
        itemMap.add(new ItemPack("drill", 3, 1,
                Items.titanium
        ));
        itemMap.add(new ItemPack("craft", 4, 1,
                Items.plastanium, Items.surgeAlloy, Items.sporePod
        ));
        itemMap.add(new ItemPack("drill", 5, 2,
                Items.thorium
        ));
        itemMap.add(new ItemPack("craft", 6, 1,
                Items.phaseFabric, Items.blastCompound
        ));
    }

    public static int getTierOfItem(Item item) {
        for (ItemPack pack : itemMap) if (pack.all.contains(item)) return pack.tier;
        return -1;
    }

    public static Seq<ItemPack> getItemMapCopy() {
        Seq<ItemPack> newSeq = new Seq<>();
        for (ItemPack pack : itemMap) newSeq.add(pack.copy());
        return newSeq;
    }

    public static int getRange(int minTier, int maxTier) {
        int range = 0;
        for (int i = minTier; i < maxTier; i++) range += getPackByTier(i).all.size;
        return range;
    }

    public static ItemStack[] getRandomItemStacks(int maxTier, int maxItemStackCount, int maxItemCount, boolean unique) {
        Seq<ItemPack> copy = getItemMapCopy();
        Seq<ItemStack> seq = new Seq<>();
        int minTier = Math.max(maxTier - 2, 0);
        int itemStackCount = Math.min(getRandomInt(maxItemStackCount) + 1, getRange(minTier, maxTier));
        for (int i = 0; i < itemStackCount; i++) {
            int count = getRandomInt(maxItemCount) + 1;
            int tier = getRandomInt(minTier, maxTier);
            Item item = getRandomByPack(getPackByTier(tier, copy), true);
            if (item == null) continue;
            seq.add(new ItemStack(item, count));
        }
        return seq.toArray(ItemStack.class);
    }

    public static Item getRandomItem(boolean lock) {
        return getRandomByPack(itemMap.random(Main.rand), lock);
    }

    public static Item getGenericCrafterOut() {
        return getRandomByPack(getPacksByTag("craft").random(Main.rand), true);
    }

    public static Item getRandomByPack(ItemPack pack, boolean lock) {
        if (lock) {
            Item item = pack.locked.random(Main.rand);
            if (item == null) return null;
            pack.locked.remove(item);
            return item;
        }
        return pack.all.random(Main.rand);
    }

    public static ItemPack getPackByTagAndLocalTier(String tag, int tier) {
        Seq<ItemPack> seq = getPacksByTag(tag);
        for (ItemPack pack : itemMap) if (pack.localTier == tier) return pack;
        return null;
    }

    public static Seq<ItemPack> getPacksByTag(String tag) {
        Seq<ItemPack> seq = new Seq<>();
        for (ItemPack pack : itemMap) if (pack.tag.equalsIgnoreCase(tag)) seq.add(pack);
        return seq;
    }

    public static Seq<ItemPack> getPacksByLocalTier(int tier) {
        Seq<ItemPack> seq = new Seq<>();
        for (ItemPack pack : itemMap) if (pack.localTier == tier) seq.add(pack);
        return seq;
    }

    public static ItemPack getPackByTier(int tier) {
        return getPackByTier(tier, itemMap);
    }

    public static ItemPack getPackByTier(int tier, Seq<ItemPack> itemMap) {
        ItemPack pack = null;
        for (ItemPack newPack : itemMap) if (newPack.tier == tier) pack = newPack;
        return pack;
    }

    public static int getRandomInt(int max) {
        return Main.rand.nextInt(max);
    }

    public static int getRandomInt(int min, int max) {
        return min + getRandomInt(max - min);
    }
}
