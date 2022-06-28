package randomMindustry;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;

import mindustry.type.Item;
import randomMindustry.random.mappers.BlockMapper;
import randomMindustry.random.mappers.RegionMapper;
import randomMindustry.random.mappers.ResourceMapper;
import randomMindustry.random.util.RandomUtil;
import randomMindustry.settings.SettingsLoader;
import randomMindustry.ui.dialogs.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class Main extends Mod {
    public Main() {
        Events.on(ClientLoadEvent.class, e -> client());
        Events.on(ServerLoadEvent.class, e -> server());
    }

    public static void client() {
        ui.paused.buttons.button(Icon.effect, Dialogs.menuDialog::show).size(210f, 64f);
        SettingsLoader.init();
        load();
        settings.put("rm-seed", Long.toString(RandomUtil.getSeed()));
        netClient.addPacketHandler("seed", (str) -> {
            load();
            generate();
        });
    }

    public static void server() {
        load();
        Events.on(PlayerConnectionConfirmed.class, (playerConnectionConfirmed) -> {
            Call.clientPacketReliable(playerConnectionConfirmed.player.con, "seed", Long.toString(RandomUtil.getSeed()));
        });
        Events.on(WorldLoadEvent.class, (worldLoadEvent) -> {
            load();
            Call.clientPacketReliable("seed", Long.toString(RandomUtil.getSeed()));
            Call.infoPopup("selected seed:" + RandomUtil.getSeed(), 60, Align.left, 0, 0, 0, 0);
        });
    }

    public static void load() {
        RandomUtil.newSeed();
        Log.info("seed is " + RandomUtil.getSeed());
        generate();
    }

    public static void generate() {
        Log.info(bundle.get("msg.rm-log-generating"));
        RegionMapper.init();
        ResourceMapper.init();
        BlockMapper.init();
        for (ResourceMapper.ItemPack pack : ResourceMapper.itemMap) {
            Log.info("=====" + pack.tag + ":" + pack.tier + "." + pack.localTier);
            for (Item item : pack.all) {
                if (pack.locked.contains(item)) Log.info("[red]==LOCKED==");
                if (item == null) Log.info("some null item :skull:");
                else Log.info(item + item.emoji());
            }
        }
        Log.info(bundle.get("msg.rm-log-generated"));
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("seed", "sends seed and syncs", (arr, player) -> {
            player.sendMessage("seed is " + RandomUtil.getSeed());
            Call.clientPacketReliable(player.con, "seed", Long.toString(RandomUtil.getSeed()));
        });
    }
}
