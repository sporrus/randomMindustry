package randomMindustry;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.type.*;

import java.util.Random;

public class Main extends Mod{
    public static Rand rand;

    public Main(){
        Events.on(ClientLoadEvent.class, (e) -> {
            long seed = new Random().nextLong();
            rand = new Rand(seed);
            Core.settings.put("rm-seed", Long.toString(seed));
            SettingsLoader.init();
            generate();
        });
    }

    public static void generate() {
        Log.info("[green]===========GENERATING!===========");
        ResourceMapper.init();
        BlockMapper.init();
        BulletMapper.init();
        for (ItemPack pack : ResourceMapper.itemMap) {
            Log.info(pack.tag + " " + pack.tier + " locked:");
            for (Item item : pack.locked) Log.info(item + item.emoji());
        }
        Log.info("[green]===========GENERATED!===========");
    }
}
