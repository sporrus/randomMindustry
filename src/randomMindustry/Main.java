package randomMindustry;

import arc.*;
import arc.graphics.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.g3d.*;
import mindustry.mod.*;
import mindustry.type.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.block.blocks.*;
import randomMindustry.mappers.item.*;
import randomMindustry.random.*;
import randomMindustry.texture.*;
import randomMindustry.ui.Settings;

import static randomMindustry.mappers.block.BlockMapper.r;

public class Main extends Mod {
    public Main() {

    }

    @Override
    public void init() {
        Vars.content.planets().select(p -> p != RandomPlanets.random).each(p -> p.hiddenItems.addAll(ItemMapper.generatedItems));
        if (!Vars.headless) {
            Settings.load();
            TextureManager.init();
            Vars.netClient.addPacketHandler("seed", s -> {
                try {
                    SeedManager.setSeed(Long.parseLong(s));
                    reload();
                } catch (NumberFormatException e) {
                    Log.err("error parsing seed packet", e);
                }
            });
        }
        Events.on(EventType.PlayerJoin.class, e -> {
            Call.clientPacketReliable(e.player.con, "seed", String.valueOf(SeedManager.getSeed()));
        });
        Events.on(EventType.WorldLoadEvent.class, e -> {
            if (Vars.headless) {
                SeedManager.generateSeed();
                reload();
            }
            Call.clientPacketReliable("seed", String.valueOf(SeedManager.getSeed()));
            Log.info("selected seed:" + SeedManager.getSeed());
        });

//        Events.on(EventType.SaveLoadEvent.class, e -> Core.app.post(() -> {
//            if (Vars.state.isCampaign()) {
//                long seed = Core.settings.getLong("rm-campaign-seed");
//                if (Core.settings.get("rm-campaign-seed", null) == null) {
//                    seed = SeedManager.getSeed();
//                } else if (seed != SeedManager.getSeed()) {
//                    Log.info("loaded campaign " + seed);
//                    SeedManager.setSeed(seed);
//                    reload();
//                }
//                Log.info("saved campaign " + seed);
//                Core.settings.put("rm-campaign-seed", seed);
//            } else {
//                long seed = Vars.state.rules.tags.getLong("rm-seed");
//                if (Vars.state.rules.tags.get("rm-seed", (String)null) == null) {
//                    seed = SeedManager.getSeed();
//                } else if (seed != SeedManager.getSeed()) {
//                    Log.info("loaded " + seed);
//                    SeedManager.setSeed(seed);
//                    reload();
//                }
//                Log.info("saved " + seed);
//                Vars.state.rules.tags.put("rm-seed", String.valueOf(seed));
//            }
//        }));

// debug atlas:
//        final int[] i = {0};
//        TextureManager.getAllTextures().each((t) -> {
//            PixmapIO.writePng(new Fi("atlas" + (i[0]++) + ".png"), t.getTextureData().getPixmap());
//        });

// debug packs:
//        Log.info("DEBUG PACKS:");
//        for (ItemPack pack : ItemMapper.packs) {
//            Log.info(pack.tier + ":" + pack.localTier + " (" + pack.globalTier + ")");
//            for (CustomItem item : pack.all) {
//                Log.info("      " + item.name + (pack.locked(item) ? " [red]LOCKED[]" : ""));
//            }
//        }
    }

    @Override
    public void registerClientCommands(CommandHandler h) {
        h.<Player>register("seed", "syncs seeds", (args, player) ->
                Call.clientPacketReliable(player.con, "seed", String.valueOf(SeedManager.getSeed())));
    }

    public static void reload() {
        if (!Vars.headless) TextureManager.reload();
        ItemMapper.reloadContent();
        BlockMapper.reloadContent();
    }

    @Override
    public void loadContent() {
        try {
            SeedManager.setSeed(Long.parseLong(Core.settings.getString("rm-seed")));
        } catch (NumberFormatException e) {
            SeedManager.generateSeed();
        }
        ItemMapper.generateContent();
        BlockMapper.generateContent();
        RandomLoadouts.load();
        RandomPlanets.load();
        RandomPlanets.random.orbitOffset = RandomPlanets.star.orbitOffset = 0f;
        if (!Vars.headless) RandomTechTree.load();
    }
}
