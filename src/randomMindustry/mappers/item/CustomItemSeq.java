package randomMindustry.mappers.item;

import arc.func.*;
import arc.math.*;
import arc.struct.*;

public class CustomItemSeq extends Seq<CustomItem> {
    public CustomItemSeq(CustomItemSeq seq) {
        super(seq);
    }

    public CustomItemSeq(Seq<CustomItem> seq) {
        super(seq);
    }

    public CustomItemSeq() {}

    public CustomItemSeq selectGlobalTier(int globalTier) {
        return select(i -> i.globalTier == globalTier);
    }

    public CustomItemSeq selectGlobalTierRange(int min, int max) {
        return select(i -> i.globalTier >= min && i.globalTier <= max);
    }

    public CustomItemSeq selectLocalTier(int localTier) {
        return select(i -> i.localTier == localTier);
    }

    public CustomItemSeq selectTierType(ItemTierType tierType) {
        return select(i -> i.tierType == tierType);
    }

    public CustomItemSeq selectLocked(boolean locked) {
        return select(i -> i.locked == locked);
    }

    public CustomItemSeq select(Boolf<CustomItem> predicate){
        return new CustomItemSeq(super.select(predicate));
    }

    public CustomItemSeq copy(){
        return new CustomItemSeq(this);
    }

    public CustomItem lockRandom(Rand r, boolean lock) {
        CustomItem item = random(r);
        item.locked = lock;
        if (!lock) remove(item);
        return item;
    }

    public CustomItem next() {
        return size > 0 ? get(0) : null;
    }

    public CustomItem lockNext(boolean lock) {
        CustomItem next = next();
        if (next == null) return null;
        next.locked = lock;
        if (!lock) remove(next);
        return next;
    }
}
