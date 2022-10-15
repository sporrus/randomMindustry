package randomMindustry.random;

import arc.math.*;
import arc.struct.*;

public class SeedManager {
    private static long seed = 0;
    private static final Seq<SyncedRand> rands = new Seq<>();

    public static void generateSeed() {
        setSeed(new Rand().nextLong());
    }
    public static void setSeed(long seed) {
        SeedManager.seed = seed;
        updateRands();
    }
    public static long getSeed() {
        return seed;
    }
    public static void addRand(SyncedRand rand) {
        rands.add(rand);
    }
    public static void updateRands() {
        rands.each(r -> r.setSeed(seed));
    }
}
