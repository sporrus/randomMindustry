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
    public static Planet random, fakesun;
    
    public Main() {

    }

    @Override
    public void init() {
        Vars.content.planets().select(p -> p != random).each(p -> p.hiddenItems.addAll(ItemMapper.generatedItems));
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
        } else {
            Events.on(EventType.PlayerJoin.class, e -> {
                Call.clientPacketReliable(e.player.con, "seed", String.valueOf(SeedManager.getSeed()));
            });
            Events.on(EventType.WorldLoadEvent.class, e -> {
                SeedManager.generateSeed();
                reload();
                Call.clientPacketReliable("seed", String.valueOf(SeedManager.getSeed()));
                Call.infoPopup("selected seed:" + SeedManager.getSeed(), 60, Align.left, 0, 0, 0, 0);
            });
        }

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
        
        fakesun = new Planet("rm-fakesun", Planets.sun, 4f){{
            bloom = true;
            accessible = false;
            orbitRadius = 500f;
            
            meshLoader = () -> new SunMesh(
                this, 4,
                5, 0.3, 1.7, 1.2, 1,
                1.1f,
                new Color(r.random(0.5f, 1f), r.random(0.5f, 1f), r.random(0.5f, 1f)),
                new Color(r.random(0.5f, 1f), r.random(0.5f, 1f), r.random(0.5f, 1f)),
                new Color(r.random(0.5f, 1f), r.random(0.5f, 1f), r.random(0.5f, 1f)),
                new Color(r.random(0.5f, 1f), r.random(0.5f, 1f), r.random(0.5f, 1f)),
                new Color(r.random(0.5f, 1f), r.random(0.5f, 1f), r.random(0.5f, 1f)),
                new Color(r.random(0.5f, 1f), r.random(0.5f, 1f), r.random(0.5f, 1f))
            );
        }};
        
        random = new Planet("rm-random", fakesun, 1f, 3){{
            localizedName = "Random";
            generator = new RandomPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, (int) SeedManager.getSeed(), 0.15f, 0.13f, 5, new Color().set(Team.crux.color.cpy()).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                    new HexSkyMesh(this, (int) SeedManager.getSeed(), 0.6f, 0.16f, 5, Color.white.cpy().lerp(Team.crux.color.cpy(), 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
            );
            launchCapacityMultiplier = 0.5f;
            sectorSeed = (int) SeedManager.getSeed();
            allowWaves = true;
            allowWaveSimulation = true;
            allowSectorInvasion = true;
            allowLaunchSchematics = true;
            enemyCoreSpawnReplace = true;
            allowLaunchLoadout = true;
            ruleSetter = r -> {
                r.waveTeam = Team.green;
                r.placeRangeCheck = false;
                r.attributes.clear();
                r.showSpawns = true;
            };
            atmosphereColor = Team.crux.color.cpy().a(0.65f);
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            startSector = 1;
            alwaysUnlocked = true;
            defaultCore = (RandomCore)BlockMapper.generatedBlocks.find(b -> b instanceof RandomCore c && c.id == 0);
            landCloudColor = Team.crux.color.cpy().a(0.5f);
            hiddenItems.addAll(Vars.content.items()).removeAll(ItemMapper.generatedItems);
        }};
        
        RandomTechTree.load();
    }
}
