package randomMindustry.mappers.item;

import arc.struct.*;
import randomMindustry.*;

public class ItemPack {
    public final Seq<CustomItem> all, locked;
    public final String tier;
    public final int localTier, globalTier;
    public final SyncedRand r;

    public ItemPack(int localTier, int globalTier, String tier, CustomItem... all) {
        this.all = new Seq<>(all);
        this.locked = this.all.copy();
        this.localTier = localTier;
        this.globalTier = globalTier;
        this.tier = tier;
        this.r = new SyncedRand();
    }

    public boolean in(CustomItem item) {
        return all.contains(item);
    }

    public boolean locked(CustomItem item) {
        return in(item) && locked.contains(item);
    }

    public int locked() {
        return locked.size;
    }

    public void unlock(CustomItem item) {
        locked.remove(item);
    }

    public void unlock() {
        locked.clear();
        locked.addAll(all);
    }

    public CustomItem random(boolean lock) {
        if (lock) {
            CustomItem item = locked.random(r);
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

    public void add(CustomItem item, boolean isLocked) {
        all.add(item);
        if (isLocked) locked.add(item);
    }
}
