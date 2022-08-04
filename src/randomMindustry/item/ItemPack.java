package randomMindustry.item;

import arc.math.*;
import arc.struct.*;
import randomMindustry.*;

public class ItemPack {
    public final Seq<CustomItem> all, locked;
    public final String tier;
    public final int localTier, globalTier;
    public final Rand rand;

    public ItemPack(int localTier, int globalTier, String tier, CustomItem... all) {
        this.all = new Seq<>(all);
        this.locked = this.all.copy();
        this.localTier = localTier;
        this.globalTier = globalTier;
        this.tier = tier;
        this.rand = new Rand(SeedManager.getSeed());
    }

    public int locked() {
        return locked.size;
    }

    public void lock() {
        locked.clear();
        locked.addAll(all);
    }

    public CustomItem random(boolean lock) {
        if (lock) {
            CustomItem item = locked.random(rand);
            locked.remove(item);
            return item;
        } else {
            return all.random(rand);
        }
    }
}
