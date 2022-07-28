package randomMindustry.random.util;

import arc.math.*;
import arc.struct.*;

import java.util.*;

// TODO: maybe make it extend Rand? seems lame to type getRand() everytime
public class RandomUtil {
    private static final Rand rand = new Rand();
    private static final Rand clientRand = new Rand();
    private static long seed;

    public static <T> T random(T[] arr, Rand rand) {
        return arr[rand.nextInt(arr.length)];
    }

    public static <T> void shuffle(Seq<T> seq) {
        T[] items = seq.items;
        for (int i = seq.size - 1; i >= 0; i--) {
            int ii = rand.random(i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    public static int getRandomIntMultClient(int max, int mult) {
        int num = clientRand.random(max);
        num -= num % mult;
        return num;
    }

    public static float getRandomFloatMultClient(float max, float mult) {
        float num = clientRand.random(max);
        num -= num % mult;
        return num;
    }

    public static float getRandomFloatMultClient(float min, float max, float mult) {
        float num = clientRand.random(max - min) + min;
        num -= num % mult;
        return num;
    }

    public static int getRandomIntMultClient(int min, int max, int mult) {
        int num = clientRand.random(max - min) + min;
        num -= num % mult;
        return num;
    }

    public static int getRandomIntMult(int max, int mult) {
        int num = rand.random(max);
        num -= num % mult;
        return num;
    }

    public static float getRandomFloatMult(float max, float mult) {
        float num = rand.random(max);
        num -= num % mult;
        return num;
    }

    public static float getRandomFloatMult(float min, float max, float mult) {
        float num = rand.random(max - min) + min;
        num -= num % mult;
        return num;
    }

    public static int getRandomIntMult(int min, int max, int mult) {
        int num = rand.random(max - min) + min;
        num -= num % mult;
        return num;
    }

    public static void newSeed() {
        seed = new Random().nextLong();
        setSeed(seed);
    }

    public static long getSeed() {
        return seed;
    }

    public static void setSeed(long seed) {
        RandomUtil.seed = seed;
        rand.setSeed(seed);
        clientRand.setSeed(seed);
    }

    public static Rand getRand() {
        return rand;
    }
    public static Rand getClientRand() {
        return clientRand;
    }
}
