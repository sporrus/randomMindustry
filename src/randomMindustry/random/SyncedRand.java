package randomMindustry.random;

import arc.math.*;

public class SyncedRand extends Rand {
    public SyncedRand() {
        setSeed(SeedManager.getSeed());
        SeedManager.addRand(this);
    }
}
