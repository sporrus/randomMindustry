package randomMindustry;

import arc.Events;
import arc.util.Log;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mod;
import mindustry.type.Item;

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
