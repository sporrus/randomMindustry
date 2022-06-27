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
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

import java.util.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class Main extends Mod {
    public static long seed;
    public static Rand rand;

    public Main() {
        Events.on(ClientLoadEvent.class, e -> {
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
        BulletMapper.init();
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
        handler.<Player>register("find", "<item>", "Finds factories with output as item", (arr, player) -> {
            String itemName = arr[0];
            StringBuilder cost = new StringBuilder();
            content.blocks().select((block -> {
                if (!(block instanceof GenericCrafter)) return false;
                ItemStack[] items = ((GenericCrafter) block).outputItems;
                if (items == null) return false;
                for (ItemStack itemStack : items) if (itemStack.item.name.equalsIgnoreCase(itemName)) return true;
                return false;
            })).each(block -> {
                cost.append(block.name).append(" ").append(block.emoji()).append(" => ");
                for (Consume consume : block.consumers) {
                    if (consume instanceof ConsumeItems) {
                        ItemStack[] items = ((ConsumeItems) consume).items;
                        for (ItemStack itemStack : items)
                            cost.append(itemStack.amount).append(itemStack.item.emoji()).append(" ");
                    }
                }
                cost.append("=> ");
                for (ItemStack stack : ((GenericCrafter) block).outputItems) {
                    cost.append(stack.amount).append(stack.item.emoji()).append(" ");
                }
                cost.append("\n");
            });
            Seq<ItemPack> ores = ResourceMapper.getPacksByTag("drill").addAll(ResourceMapper.getPacksByTag("hand"));
            ores.each(pack -> {
                pack.all.each(item -> {
                    if (item.name.equalsIgnoreCase(itemName)) {
                        if (pack.tag.equalsIgnoreCase("hand"))
                            cost.append("hands ").append(UnitTypes.alpha.emoji()).append(" => ").append(item.emoji());
                        else if (pack.localTier == 0)
                            cost.append(Blocks.mechanicalDrill.name).append(" ").append(Blocks.mechanicalDrill.emoji()).append(" => ").append(item.emoji());
                        else if (pack.localTier == 1)
                            cost.append(Blocks.pneumaticDrill.name).append(" ").append(Blocks.pneumaticDrill.emoji()).append(" => ").append(item.emoji());
                        else if (pack.localTier == 2)
                            cost.append(Blocks.laserDrill.name).append(" ").append(Blocks.laserDrill.emoji()).append(" => ").append(item.emoji());
                    }
                });
            });
            player.sendMessage(cost.toString());
        });
        handler.<Player>register("seed", "sends seed and syncs", (arr, player) -> {
            player.sendMessage("seed is " + seed);
            Call.clientPacketReliable(player.con, "seed", Long.toString(seed));
        });
    }
}
