package randomMindustry.mappers.item;

import arc.func.*;
import arc.graphics.*;
import arc.struct.*;
import mindustry.type.*;
import randomMindustry.*;
import randomMindustry.mappers.Mapper;

public class ItemMapper implements Mapper {
    public static final Seq<CustomItem> generatedItems = new Seq<>();
    public static final Seq<ItemPack> packs = new Seq<>();
    public static final int itemCount = 36;
    public static final SyncedRand r = new SyncedRand();
    public static final int maxTier = itemCount / 3;

    public void editContent() {
        generatedItems.each(CustomItem::edit);
    }

    public void generateContent() {
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = new CustomItem("random-item-" + i);
            generatedItems.add(item);
        }
        ItemPack all = new ItemPack(0, 0, "all", generatedItems.toArray(CustomItem.class));
        for (int i = 0; i < itemCount / 3; i++) {
            packs.add(new ItemPack(i / 2, i, i % 2 == 0 ? "drill" : "craft",
                    all.random(true), all.random(true), all.random(true)
            ));
        }
        getPacksByTier("drill").each(pack -> pack.all.each(i -> i.hardness = pack.localTier));
    }

    public static ItemStack[] getItemStacks(int tier, int itemCount, Prov<Integer> itemAmount) {
        tier = Math.max(tier, 1);
        itemCount = Math.max(itemCount, 1);
        Seq<ItemStack> stacks = new Seq<>();
        ItemPack packs = ItemMapper.combine(ItemMapper.getPacksInGlobalTierRange(0, tier - 1));
        packs.lock();
        for (int i = 0; i < itemCount; i++) {
            if (i == 0) {
                CustomItem it = ItemMapper.getPackByGlobalTier(tier - 1).random(false);
                stacks.add(new ItemStack(it, itemAmount.get()));
                packs.lock(it);
            } else {
                stacks.add(new ItemStack(packs.random(true), itemAmount.get()));
            }
        }
        RandomUtil.shuffle(stacks, r);
        return stacks.toArray(ItemStack.class);
    }

    public static int getDrillItems() {
        return (int) (Math.ceil(itemCount / 6f) * 3);
    }

    public static int getCraftItems() {
        return itemCount / 6 * 3;
    }

    public static ItemPack getPack(CustomItem item) {
        Seq<ItemPack> tierPacks = packs.select((p) -> p.globalTier >= 0);
        for (ItemPack pack : tierPacks)
            if (pack.in(item)) return pack;
        return null;
    }

    public static int getTier(CustomItem item) {
        ItemPack pack = getPack(item);
        return pack == null ? -1 : pack.globalTier;
    }

    public static int getLocalTier(CustomItem item) {
        Seq<ItemPack> tierPacks = packs.select((p) -> p.globalTier >= 0);
        for (ItemPack pack : tierPacks)
            if (pack.in(item)) return pack.localTier;
        return -1;
    }

    public static ItemPack combine(Seq<ItemPack> itemPacks) {
        return combine(itemPacks.toArray(ItemPack.class));
    }

    public static ItemPack combine(ItemPack... itemPacks) {
        ItemPack pack = new ItemPack(-1, -1, "all");
        for (ItemPack p : itemPacks) pack.addFrom(p);
        return pack;
    }

    public static Seq<ItemPack> getPacksWithItem(CustomItem item, boolean locked) {
        return getPacksBy((pack) -> (locked ? pack.locked : pack.all).contains(item));
    }

    public static Seq<ItemPack> getPacksInGlobalTierRange(int min, int max) {
        return getPacksBy((pack) -> pack.globalTier >= min && pack.globalTier <= max);
    }

    public static Seq<ItemPack> getPacksByTier(String tier) {
        return getPacksBy((pack) -> pack.tier.equalsIgnoreCase(tier));
    }

    public static Seq<ItemPack> getLockedPacksByTier(String tier) {
        return getPacksByTier(tier).select((pack) -> pack.locked() != 0);
    }

    public static ItemPack getPackByGlobalTier(int globalTier) {
        return getFirstPackBy((pack) -> pack.globalTier == globalTier);
    }

    public static void lock(CustomItem item) {
        getPacksWithItem(item, true).select(p -> p.globalTier >= 0).each(p -> p.lock(item));
    }

    public static ItemPack getFirstPackBy(Func<ItemPack, Boolean> func) {
        for (ItemPack pack : packs)
            if (func.get(pack)) return pack;
        return null;
    }

    public static Seq<ItemPack> getPacksBy(Func<ItemPack, Boolean> func) {
        Seq<ItemPack> itemPacks = new Seq<>();
        for (ItemPack pack : packs)
            if (func.get(pack)) itemPacks.add(pack);
        return itemPacks;
    }
}
