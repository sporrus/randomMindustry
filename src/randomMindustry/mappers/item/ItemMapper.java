package randomMindustry.mappers.item;

import arc.func.*;
import arc.struct.*;
import mindustry.type.*;
import randomMindustry.random.*;

public class ItemMapper {
    public static final CustomItemSeq generatedItems = new CustomItemSeq();
    public static final int itemCount = 36;
    public static final SyncedRand r = new SyncedRand();
    public static final int maxTier = itemCount / 3;

    public static void generateContent() {
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = new CustomItem("random-item-", i);
            generatedItems.add(item);
        }
    }

    public static void reloadContent() {
        generatedItems.each(CustomItem::reload);
    }

    public static ItemStack[] getItemStacks(int tier, int itemCount, Prov<Integer> itemAmount) {
        tier = Math.max(tier, 0);
        Seq<ItemStack> stacks = new Seq<>();
        CustomItemSeq items = generatedItems.selectGlobalTierRange(0, tier);
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
