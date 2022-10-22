package randomMindustry.mappers.item;

import arc.func.*;
import arc.math.*;
import arc.struct.*;
import mindustry.*;
import mindustry.type.*;
import randomMindustry.random.*;

public class ItemMapper {
    public static final CustomItemSeq generatedItems = new CustomItemSeq();
    public static final int itemCount = 36;
    public static final SyncedRand r = new SyncedRand();
    public static final SyncedRand cr = new SyncedRand();
    public static final int maxTier = itemCount / 3;

    public static void generateContent() {
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = new CustomItem("random-item-", i);
            generatedItems.add(item);
        }
    }

    public static void reloadContent() {
        generatedItems.each(CustomItem::generate);
        if (!Vars.headless) generatedItems.each(CustomItem::reloadIcons);
        generatedItems.each(CustomItem::init);
    }

    public static ItemStack[] getItemStacks(int tier, int itemCount, Prov<Integer> itemAmount, Rand r) {
        tier = Math.max(tier, 1);
        Seq<ItemStack> stacks = new Seq<>();
        CustomItemSeq items = generatedItems.selectGlobalTierRange(1, tier);
        itemCount = Math.min(Math.max(itemCount, 1), items.size);
        for (int i = 0; i < itemCount; i++) {
            CustomItem it = i == 0 ? generatedItems.selectGlobalTier(tier).random(r) : items.random(r);
            stacks.add(new ItemStack(it, itemAmount.get()));
            items.remove(it);
        }
        RandomUtil.shuffle(stacks, r);
        return stacks.toArray(ItemStack.class);
    }
}
