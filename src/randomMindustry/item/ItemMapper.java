package randomMindustry.item;

import arc.func.*;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;

public class ItemMapper {
    public static final Seq<CustomItem> generatedItems = new Seq<>();
    public static final Seq<ItemPack> packs = new Seq<>();
    public static final int itemCount = 32;
    public static final Rand rand = new Rand();

    public static void editContent() {
        rand.setSeed(SeedManager.getSeed());
        generatedItems.each(CustomItem::edit);

        packs.add(new ItemPack(0, 0, "drill",
                generatedItems.random(rand), generatedItems.random(rand), generatedItems.random(rand)
        ));

        ((GenericCrafter) Blocks.graphitePress).outputItems = new ItemStack[]{new ItemStack(
                getPackByGlobalTier(0).random(true), 5
        )};
    }

    public static void generateContent() {
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = new CustomItem("random-item-" + i, Color.red);
            generatedItems.add(item);
        }
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

    public static ItemPack getPackByGlobalTier(int globalTier) {
        return getFirstPackBy((pack) -> pack.globalTier == globalTier);
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
