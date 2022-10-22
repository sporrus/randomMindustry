package randomMindustry.mappers.sound;

import arc.audio.*;
import arc.struct.*;
import arc.util.*;
import mindustry.gen.*;
import randomMindustry.random.*;

public class SoundMapper {
    public static final SyncedRand r = new SyncedRand();
    public static final int maxId = Sounds.class.getFields().length - 2;

    public static Sound randomShoot() {
        return Seq.with(
                Sounds.pew, Sounds.shoot, Sounds.shootSnap
        ).random(r);
    }

    public static Sound random() {
        return Sounds.getSound(r.random(0, maxId - 1));
    }
}
