package randomMindustry.random.mappers;

import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import randomMindustry.random.util.*;
import randomMindustry.util.techTrees.*;

public class ResourceMapper {
    public static Seq<ItemPack> itemMap = new Seq<>();

    // TODO: make randomized items
    public static void init() {
        Seq<Item> items = Vars.content.items().select((item) -> TechUtil.getRoot(item).contains(Planets.serpulo));
        RandomUtil.shuffle(items);
        items.each((item -> {
            item.stats = new Stats();
            item.stats.intialized = true;
            item.explosiveness = (RandomUtil.getRand().chance(0.5)) ? RandomUtil.getRand().random(4) / 4f : 0;
            item.flammability = (RandomUtil.getRand().chance(0.5)) ? RandomUtil.getRand().random(4) / 4f : 0;
            item.radioactivity = (RandomUtil.getRand().chance(0.5)) ? RandomUtil.getRand().random(4) / 4f : 0;
            item.charge = (RandomUtil.getRand().chance(0.5)) ? RandomUtil.getRand().random(4) / 4f : 0;
            item.cost = RandomUtil.getRand().random(100) / 100f;
            item.setStats();
        }));
        ItemPack all = new ItemPack("all", 0, 0, items.toArray(Item.class));
        Log.info(items.size);

        itemMap = new Seq<>();
        itemMap.add(new ItemPack("hand", 0, 0,
                all.random(true), all.random(true), all.random(true)
        ));
        itemMap.add(new ItemPack("drill", 1, 0,
                all.random(true), all.random(true)
        ));
        itemMap.add(new ItemPack("craft", 2, 0,
                all.random(true), all.random(true), all.random(true)
        ));
        itemMap.add(new ItemPack("drill", 3, 1,
                all.random(true)
        ));
        itemMap.add(new ItemPack("craft", 4, 1,
                all.random(true), all.random(true), all.random(true)
        ));
        itemMap.add(new ItemPack("drill", 5, 2,
                all.random(true)
        ));
        itemMap.add(new ItemPack("craft", 6, 1,
                all.random(true), all.random(true), all.random(true)
        ));

        ItemPack ores = combine(false, getPackByTier(0), getPackByTier(1), getPackByTier(3), getPackByTier(5));
        new Seq<>(new Block[]{Blocks.oreCopper, Blocks.oreLead, Blocks.oreScrap, Blocks.oreCoal, Blocks.sand, Blocks.oreTitanium, Blocks.oreThorium}).each(b -> {
            Item item = ores.random(true);
            item.hardness = (getTierOfItem(item) + 1) / 2 + 1;
            item.lowPriority = b == Blocks.sand;
            b.itemDrop = item;
            unlock(item);
        });
        Blocks.darksand.itemDrop = Blocks.sand.itemDrop;
        Blocks.darksand.localizedName = "Dark " + Blocks.sand.itemDrop.localizedName;
        Blocks.sand.localizedName = Blocks.sand.itemDrop.localizedName;
        Blocks.sand.playerUnmineable = false;
        Blocks.darksand.playerUnmineable = false;
    }

    public static void unlock(Item item) {
        for (ItemPack pack : itemMap) pack.locked.remove(item);
    }

    public static ItemPack combine(boolean makeNew, ItemPack... packs) {
        ItemPack pack = new ItemPack("none", -1, -1);
        for (ItemPack itemPack : packs) {
            pack.all.add(itemPack.all);
            pack.locked.add(makeNew ? itemPack.all : itemPack.locked);
        }
        return pack;
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
        for (int i = minTier; i < maxTier; i++) {
            ItemPack pack = getPackByTier(i);
            if (pack == null) continue;
            range += pack.all.size;
        }
        return range;
    }

    public static ItemStack[] getRandomItemStacks(int maxTier, int maxItemStackCount, int maxItemCount, int itemMult, boolean unique) {
        Seq<ItemStack> seq = new Seq<>();
        int minTier = Math.max(maxTier - 2, 0);
        ItemPack all = getPackByTier(minTier).copy();
        all.relock();
        for (int i = minTier + 1; i < maxTier; i++) {
            ItemPack pack = getPackByTier(i).copy();
            pack.relock();
            all = combine(true, all, pack);
        }
        int itemStackCount = Math.min(RandomUtil.getRand().random(1, maxItemStackCount), getRange(minTier, maxTier));
        for (int i = 0; i < itemStackCount; i++) {
            int count = RandomUtil.getRandomIntMult(Math.max(0, maxItemCount - 100), maxItemCount - itemMult, itemMult) + itemMult;
            seq.add(new ItemStack(all.random(true), count));
        }
        return seq.toArray(ItemStack.class);
    }

    public static Item getRandomItem(boolean lock) {
        return itemMap.random(RandomUtil.getRand()).random(lock);
    }

    public static Item getGenericCrafterOut() {
        return getPacksByTag("craft").random(RandomUtil.getRand()).random(true);
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
        for (ItemPack newPack : itemMap) if (newPack.tier == tier) return newPack;
        return null;
    }

    public static class ItemPack {
        public Seq<Item> locked = new Seq<>();
        public Seq<Item> all = new Seq<>();
        public String tag = "null";
        public int tier = 0, localTier = 0;

        public ItemPack(String tag, int tier, int localTier, Item... items) {
            all = new Seq<>(items);
            locked = all.copy();
            this.tag = tag;
            this.tier = tier;
            this.localTier = localTier;
        }

        public Item random(boolean lock) {
            if (lock) {
                Item item = locked.random(RandomUtil.getRand());
                if (item == null) return null;
                locked.remove(item);
                return item;
            }
            return all.random(RandomUtil.getRand());
        }

        public void relock() {
            this.all = new Seq<>(this.locked.toArray(Item.class));
        }

        public ItemPack copy() {
            return new ItemPack(tag, tier, localTier, all.toArray(Item.class));
        }

        @Override
        public String toString() {
            return "ItemPack{" +
                    "locked=" + locked +
                    ", all=" + all +
                    ", tag='" + tag + '\'' +
                    ", tier=" + tier +
                    ", localTier=" + localTier +
                    '}';
        }
    }
}
