package randomMindustry.random.util;

import arc.math.Rand;
import arc.struct.Seq;
import randomMindustry.Main;

import java.util.Random;

public class RandomUtil {
    private static final Rand rand = new Rand();
    private static long seed;

    public static <T> void shuffle(Seq<T> seq) {
        T[] items = seq.items;
        for (int i = seq.size - 1; i >= 0; i--) {
            int ii = rand.random(i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    public static int getRandomIntMult(int max, int mult) {
        int num = rand.nextInt(max);
        num -= num % mult;
        return num + mult;
    }

    public static int getRandomIntMult(int min, int max, int mult) {
        int num = rand.nextInt(max - min) + min;
        num -= num % mult;
        return num + mult;
    }

    public static void newSeed() {
        seed = new Random().nextLong();
        rand.setSeed(seed);
    }

    public static long getSeed() {
        return seed;
    }

    public static void setSeed(long seed) {
        RandomUtil.seed = seed;
        rand.setSeed(seed);
    }

    public static Rand getRand() {
        return rand;
    }
}
