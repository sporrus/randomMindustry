package randomMindustry;

import arc.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.type.*;

public class Main extends Mod{
    public Main(){
        Events.on(ClientLoadEvent.class, (e) -> {
            SettingsLoader.init();

            ResourceMapper.init();
            BlockMapper.init();
            BulletMapper.init();
            for (ItemPack pack : ResourceMapper.itemMap.values()) {
                Log.info(pack.tag + " " + pack.tier + " locked:");
                for (Item item : pack.locked) Log.info(item + item.emoji());
            }
        });
    }
}
