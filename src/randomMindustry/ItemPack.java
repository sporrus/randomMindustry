package randomMindustry;

import arc.struct.*;
import mindustry.type.*;

public class ItemPack {
    public Seq<Item> locked = new Seq<>();
    public Seq<Item> all = new Seq<>();
    public String tag = "null";
    public int tier = 0, localTier = 0;

    public ItemPack(String tag, int tier, int localTier, Item... items) {
        all.add(items);
        locked = all.copy();
        this.tag = tag;
        this.tier = tier;
        this.localTier = localTier;
    }

    public ItemPack copy() {
        ItemPack newPack = new ItemPack(tag, tier, localTier, all.toArray(Item.class));
        return newPack;
    }
}
