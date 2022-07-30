package randomMindustry.random.mappers;

import arc.struct.Seq;
import mindustry.graphics.*;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import randomMindustry.random.util.*;

public class FxMapper {
    public static Seq<Effect> effects = new Seq<>();

    public static void init() {
        effects = Effect.all.select(e -> e.clip <= 50 && e != Fx.dynamicExplosion);
        effects.each(e -> e.layer = RandomUtil.getRand().random(Layer.min, Layer.max));
    }
}
