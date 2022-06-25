package randomMindustry;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.type.*;

import java.util.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class Main extends Mod{
    public static Rand rand;

    public Main(){
        Events.on(ClientLoadEvent.class, (e) -> {
            long seed = new Random().nextLong();
            rand = new Rand(seed);
            settings.put("rm-seed", Long.toString(seed));
            SettingsLoader.init();
            generate();
        });
    }

    public static void generate() {
        Log.info(bundle.get("@msg.rm-log-generating"));
        ResourceMapper.init();
        BlockMapper.init();
        BulletMapper.init();
        for (ItemPack pack : ResourceMapper.itemMap) {
            Log.info(pack.tag + " " + pack.tier + " locked:");
            for (Item item : pack.locked) Log.info(item + item.emoji());
        }
        Log.info(bundle.get("msg.rm-log-generated"));
    }
}
