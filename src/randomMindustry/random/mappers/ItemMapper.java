package randomMindustry.random.mappers;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.meta.*;
import randomMindustry.random.util.*;

public class ItemMapper {
    private static Seq<Item> selectedItems = new Seq<>();
    private static Seq<ItemPack> itemMap = new Seq<>();
    public static final int itemCount = 16;
    public static final int maxTier = 6;

    public static void init() {
        selectedItems = Vars.content.items().copy();
        RandomUtil.shuffle(selectedItems);
        selectedItems.truncate(itemCount);

        ObjectMap<Item, Float> hues = new ObjectMap<>();
        selectedItems.each((item -> {
            item.stats = new Stats();
            item.alwaysUnlocked = true;
            item.hidden = false;
            item.buildable = true;
            item.explosiveness = (RandomUtil.getRand().chance(0.5)) ? RandomUtil.getRand().random(4) / 4f : 0;
            item.flammability = (RandomUtil.getRand().chance(0.5)) ? RandomUtil.getRand().random(4) / 4f : 0;
            item.radioactivity = (RandomUtil.getRand().chance(0.5)) ? RandomUtil.getRand().random(4) / 4f : 0;
            item.charge = (RandomUtil.getRand().chance(0.5)) ? RandomUtil.getRand().random(4) / 4f : 0;
            item.cost = RandomUtil.getRand().random(100) / 100f;
            item.localizedName = StringGenerator.generateMaterialName();
            float hue = RandomUtil.getClientRand().random(360f);
            item.color.hue(hue);
            hues.put(item, hue);
            TextureGenerator.changeHue(item.fullIcon, hue);
            TextureGenerator.changeHue(item.uiIcon, hue);
            item.init();
        }));
        Seq<Item> unselectedItems = Vars.content.items().copy();
        unselectedItems.removeAll(selectedItems::contains);
        unselectedItems.each((i) -> {
            i.alwaysUnlocked = false;
            i.hidden = true;
            i.buildable = false;
        });
        ItemPack all = new ItemPack("all", 0, 0, selectedItems.toArray(Item.class));

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
        itemMap.add(new ItemPack("craft", 6, 2,
                all.random(true), all.random(true), all.random(true)
        ));

        Items.erekirOnlyItems.clear().addAll(unselectedItems);
        // TODO: this is really bad, make every planet have its own set of items
        Vars.content.planets().each((p) -> p.hiddenItems.clear().addAll(unselectedItems));
        Vars.state.rules.hiddenBuildItems.clear();
        Vars.state.rules.hiddenBuildItems.addAll(unselectedItems);

        ItemPack ores = combine(false, getPackByTier(0), getPackByTier(1), getPackByTier(3), getPackByTier(5));
        new Seq<>(new Block[]{Blocks.oreCopper, Blocks.oreLead, Blocks.oreScrap, Blocks.oreCoal, Blocks.sand, Blocks.oreTitanium, Blocks.oreThorium}).each(b -> {
            Item item = ores.random(true);
            item.hardness = (getTierOfItem(item) + 1) / 2 + 1;
            item.lowPriority = b == Blocks.sand;
            if (b instanceof OreBlock ob) {
                ob.setup(item);
                for (int i = 0; i < ob.variantRegions.length; i++)
                    TextureGenerator.changeHue(ob.variantRegions[i], hues.get(item));
                ob.init();
            } else {
                b.itemDrop = item;
            }
            unlock(item);
        });
        Blocks.darksand.itemDrop = Blocks.sand.itemDrop;
        Blocks.darksand.localizedName = "Dark " + Blocks.sand.itemDrop.localizedName;
        Blocks.sand.localizedName = Blocks.sand.itemDrop.localizedName;
        Blocks.sand.playerUnmineable = false;
        Blocks.darksand.playerUnmineable = false;
    }

    public static Seq<ItemPack> getItemMap() {
        return itemMap;
    }

    public static Seq<Item> getSelectedItems() {
        return selectedItems;
    }

    public static int getMaxTier() {
        return itemMap.size - 1;
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
        for (int i = minTier; i < maxTier + 1; i++) {
            ItemPack pack = getPackByTier(i);
            if (pack == null) continue;
            range += pack.all.size;
        }
        return range;
    }

    public static ItemStack[] getRandomItemStacks(int maxTier, int maxItemStackCount, int maxItemCount, int itemMult, boolean unique) {
        maxTier = Math.max(Math.min(maxTier, ItemMapper.maxTier), 0);
        int minTier = Math.max(maxTier - 2, 0);
        ItemPack highest = getPackByTier(maxTier).copy();
        ItemPack all = getPackByTier(minTier).copy();
        all.relock();
        for (int i = minTier + 1; i < maxTier; i++) {
            ItemPack pack = getPackByTier(i).copy();
            pack.relock();
            all = combine(true, all, pack);
        }
        return getItemStacksFromPack(highest, all, maxTier, maxItemStackCount, maxItemCount, itemMult, unique);
    }

    public static ItemStack[] getItemStacksFromPack(ItemPack highest, ItemPack all, int maxTier, int maxItemStackCount, int maxItemCount, int itemMult, boolean unique) {
        Seq<ItemStack> seq = new Seq<>();
        int minTier = Math.max(maxTier - 2, 0);
        int itemStackCount = Math.min(RandomUtil.getRand().random(1, maxItemStackCount), getRange(minTier, maxTier));
        for (int i = 0; i < itemStackCount; i++) {
            int count = RandomUtil.getRandomIntMult(Math.max(0, maxItemCount - 100), maxItemCount - itemMult, itemMult) + itemMult;
            Item item = (i == 0 ? highest : all).random(unique);
            if (i == 0) all.locked.remove(item);
            if (item == null) continue;
            seq.add(new ItemStack(item, count));
        }
        return seq.toArray(ItemStack.class);
    }

    public static Item getRandomItem(boolean lock) {
        return itemMap.random(RandomUtil.getRand()).random(lock);
    }

    public static Item getGenericCrafterOut() {
        ItemPack pack = getPacksByTag("craft").copy().select((itemPack -> itemPack.locked.size > 0)).random(RandomUtil.getRand());
        if (pack == null) return null;
        return pack.random(true);
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

        public void add(Item item, boolean lock) {
            all.add(item);
            if (lock) locked.add(item);
        }

        public void relock() {
            this.locked = new Seq<>(this.all.toArray(Item.class));
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
