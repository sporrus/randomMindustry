package randomMindustry;

import arc.math.*;
import arc.struct.*;

public class RandomUtil {
    public static <T> void shuffle(Seq<T> seq, Rand rand) {
        T[] items = seq.items;
        for (int i = seq.size - 1; i >= 0; i--) {
            int ii = rand.random(i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }
}
