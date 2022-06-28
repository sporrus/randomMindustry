package randomMindustry.util;

import arc.func.Boolf;
import arc.struct.Seq;
import mindustry.world.Block;
import mindustry.world.consumers.Consume;

public class Util {
    public static void removeAllConsumers(Block block) {
        removeConsumers(block, (consume -> true));
    }

    public static void removeConsumers(Block block, Boolf<Consume> predicate) {
        Seq<Consume> remove = new Seq<>(block.consumers).select(predicate);
        Seq<Consume> save = new Seq<>(block.consumers);
        save.removeAll(remove);
        block.consumers = new Consume[0];
        for (Consume consume : remove) block.removeConsumer(consume);
        for (Consume consume : save) block.removeConsumer(consume);
    }
}
