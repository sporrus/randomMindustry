package randomMindustry;

import arc.*;
import arc.util.*;
import mindustry.Vars;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.type.*;
import randomMindustry.random.mappers.*;
import randomMindustry.random.mappers.blocks.*;
import randomMindustry.random.util.*;
import randomMindustry.settings.*;
import randomMindustry.ui.dialogs.*;

import static arc.Core.*;
import static mindustry.Vars.*;

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
            RandomUtil.setSeed(Long.parseLong(str));
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
        if (!Vars.headless) ui.loadfrag.show("@msg.rm-generating");
        Log.info(bundle.get("msg.rm-log-generating"));
        RegionMapper.init();
        BundleMapper.init();
        ResourceMapper.init();
        BlockMapper.init();
        BulletMapper.init();
        UnitMapper.init();
        for (ResourceMapper.ItemPack pack : ResourceMapper.itemMap) {
            Log.info("=====" + pack.tag + ":" + pack.tier + "." + pack.localTier);
            for (Item item : pack.all) {
                if (pack.locked.contains(item)) Log.info("[red]==LOCKED==");
                if (item == null) Log.info("some null item :skull:");
                else Log.info(item + item.emoji());
            }
        }
        Log.info(bundle.get("msg.rm-log-generated"));
        if (!Vars.headless) ui.loadfrag.hide();
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        handler.<Player>register("seed", "sends seed and syncs", (arr, player) -> {
            player.sendMessage("seed is " + RandomUtil.getSeed());
            Call.clientPacketReliable(player.con, "seed", Long.toString(RandomUtil.getSeed()));
        });
    }
}
