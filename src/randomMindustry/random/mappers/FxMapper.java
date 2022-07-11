package randomMindustry.random.mappers;

import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.entities.Effect;

public class FxMapper {
    public static Seq<Effect> effects = new Seq<>();

    public static void init() {
        effects = Effect.all.copy();
        effects.select((e) -> e.clip <= 50 && e != Fx.dynamicExplosion);
    }
}
