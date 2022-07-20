package randomMindustry;

import arc.*;
import arc.math.*;
import arc.util.*;
import arc.scene.ui.*;
import arc.scene.event.*;
import arc.scene.actions.*;
import mindustry.Vars;
import mindustry.content.Planets;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import randomMindustry.random.mappers.*;
import randomMindustry.random.mappers.blocks.*;
import randomMindustry.random.util.*;
import randomMindustry.settings.*;
import randomMindustry.ui.dialogs.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class Main extends Mod {
    public static int phase = 1;

    public Main() {
        Events.on(ClientLoadEvent.class, e -> client());
        Events.on(ServerLoadEvent.class, e -> server());
    }

    public static void client() {
        ui.paused.buttons.button("@rm-menu", Icon.effect, Dialogs.menuDialog::show).width(220f).height(55).pad(5f).row();
        if(settings.getBool("rm-book-collected", false)) setupButtons();
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
        Events.on(PlayerConnect.class, (playerConnect) -> {
            Call.clientPacketReliable(playerConnect.player.con, "seed", Long.toString(RandomUtil.getSeed()));
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
        FxMapper.init();
        ResourceMapper.init();
        BulletMapper.init();
        UnitMapper.init();
        BlockMapper.init();
        RegionMapper.init();
        BundleMapper.init();
        for (ResourceMapper.ItemPack pack : ResourceMapper.getItemMap()) {
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
    
    public static void setupButtons(){
        ui.database.buttons.button(Icon.info, () -> Dialogs.leverDialog.show(1));
        ui.language.buttons.button(Icon.info, () -> Dialogs.leverDialog.show(2));
        ui.mods.buttons.button(Icon.info, () -> Dialogs.leverDialog.show(3));
        ui.join.buttons.button(Icon.info, () -> Dialogs.leverDialog.show(4));
        ui.host.buttons.button(Icon.info, () -> Dialogs.leverDialog.show(5));
        ui.load.buttons.button(Icon.info, () -> Dialogs.leverDialog.show(6));
        ui.settings.buttons.button(Icon.info, () -> Dialogs.leverDialog.show(7));
    }
}
