package randomMindustry;

import arc.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

import java.util.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class Main extends Mod {
    public static Rand rand;

    public Main() {
        Events.on(ClientLoadEvent.class, e -> {
            long seed = new Rand().nextLong();
            rand = new Rand(seed);
            settings.put("rm-seed", Long.toString(seed));
            SettingsLoader.init();
            generate();
        });
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
        handler.register("find", "<item>", "Finds factories with output as item", (arr) -> {
            String itemName = arr[0];
            content.blocks().select((block -> {
                if (!(block instanceof GenericCrafter)) return false;
                ItemStack[] items = ((GenericCrafter) block).outputItems;
                if (items == null) return false;
                for (ItemStack itemStack : items) if (itemStack.item.name.equalsIgnoreCase(itemName)) return true;
                return false;
            })).each(block -> {
                StringBuilder cost = new StringBuilder();
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
                Log.info(cost.toString());
            });
            Seq<ItemPack> ores = ResourceMapper.getPacksByTag("drill").addAll(ResourceMapper.getPacksByTag("hand"));
            ores.each(pack -> {
                pack.all.each(item -> {
                    if (item.name.equalsIgnoreCase(itemName)) {
                        if (pack.tag.equalsIgnoreCase("hand"))
                            Log.info("hands " + UnitTypes.alpha.emoji() + " => " + item.emoji());
                        else if (pack.localTier == 0)
                            Log.info(Blocks.mechanicalDrill.name + " " + Blocks.mechanicalDrill.emoji() + " => " + item.emoji());
                        else if (pack.localTier == 1)
                            Log.info(Blocks.pneumaticDrill.name + " " + Blocks.pneumaticDrill.emoji() + " => " + item.emoji());
                        else if (pack.localTier == 2)
                            Log.info(Blocks.laserDrill.name + " " + Blocks.laserDrill.emoji() + " => " + item.emoji());
                    }
                });
            });
        });
    }
}
