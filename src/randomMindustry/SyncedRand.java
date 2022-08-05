package randomMindustry;

import arc.math.*;

public class SyncedRand {
    public final Rand rand = new Rand();

    public SyncedRand() {
        rand.setSeed(SeedManager.getSeed());
        SeedManager.addRand(this);
    }
}
