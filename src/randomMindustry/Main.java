package randomMindustry;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.mod.*;
import mindustry.ui.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

import randomMindustry.ui.dialogs.*;

import java.util.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class Main extends Mod {
    public static long seed;
    public static Rand rand;

    public Main() {
        Events.on(ClientLoadEvent.class, e -> {
            ui.paused.buttons.button(Icon.effect, () -> {
                RMMenuDialog dialog = new RMMenuDialog();
                dialog.show();
            }).size(210f, 64f);
            
            SettingsLoader.init();
            load();
            settings.put("rm-seed", Long.toString(seed));
            netClient.addPacketHandler("seed", (str) -> {
                Call.sendMessage("generating... prev seed is "+ seed);
                seed = Long.parseLong(str);
                rand = new Rand(seed);
                Call.sendMessage("new seed is " + seed);
                generate();
                Call.sendMessage("generated!");
            });
        });

        Events.on(ServerLoadEvent.class, e -> {
            load();
            Events.on(PlayerConnectionConfirmed.class, (playerConnectionConfirmed) -> {
                Call.clientPacketReliable(playerConnectionConfirmed.player.con, "seed", Long.toString(seed));
            });
            Events.on(WorldLoadEvent.class, (worldLoadEvent) -> {
                load();
                Call.clientPacketReliable("seed", Long.toString(seed));
                Call.announce("selected seed:" + seed);
            });
        });
    }

    public static void load() {
        seed = new Rand().nextLong();
        rand = new Rand(seed);
        Log.info("seed is " + seed);
        generate();
    }

    public static <T> void shuffle(Seq<T> seq) {
        T[] items = seq.items;
        for (int i = seq.size - 1; i >= 0; i--) {
            int ii = rand.random(i);
            T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    public static Seq<Planet> getRoot(UnlockableContent content) {
        Seq<Planet> planets = new Seq<>();
        Vars.content.planets().each((planet -> {
            if (planet.techTree == null) return;
            planet.techTree.each((node) -> {
                if (node.content == content) {
                    planets.add(planet);
                }
            });
        }));
        return planets;
    }

    public static void generate() {
        Log.info(bundle.get("msg.rm-log-generating"));
        RegionMapper.init();
        ResourceMapper.init();
        BlockMapper.init();
        for (ItemPack pack : ResourceMapper.itemMap) {
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
            player.sendMessage("seed is " + seed);
            Call.clientPacketReliable(player.con, "seed", Long.toString(seed));
        });
    }
}
