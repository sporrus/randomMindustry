package randomMindustry;

import arc.math.*;

public class SeedManager {
    private static long seed;

    public static void generateSeed() {
        seed = new Rand().nextLong();
    }

    public static long getSeed() {
        return seed;
    }
}
