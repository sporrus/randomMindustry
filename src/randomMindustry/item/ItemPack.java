package randomMindustry.item;

import arc.math.*;
import arc.struct.*;
import mindustry.type.*;
import randomMindustry.*;

public class ItemPack {
    public final Seq<Item> all, locked;
    public final String tier;
    public final int localTier, globalTier;
    public final SyncedRand r;

    public ItemPack(int localTier, int globalTier, String tier, Item... all) {
        this.all = new Seq<>(all);
        this.locked = this.all.copy();
        this.localTier = localTier;
        this.globalTier = globalTier;
        this.tier = tier;
        this.r = new SyncedRand();
    }

    public boolean in(Item item) {
        return all.contains(item);
    }

    public boolean locked(Item item) {
        return in(item) && locked.contains(item);
    }

    public int locked() {
        return locked.size;
    }

    public void lock(Item item) {
        locked.remove(item);
    }

    public void lock() {
        locked.clear();
        locked.addAll(all);
    }

    public Item random(boolean lock) {
        if (lock) {
            Item item = locked.random(r);
            locked.remove(item);
            return item;
        } else {
            return all.random(r);
        }
    }

    public void addFrom(ItemPack pack) {
        all.addAll(pack.all);
        locked.addAll(pack.locked);
    }

    public void add(Item item, boolean isLocked) {
        all.add(item);
        if (isLocked) locked.add(item);
    }
}
