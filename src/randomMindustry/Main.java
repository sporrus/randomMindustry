package randomMindustry;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.game.EventType.*;
import mindustry.graphics.*;
import mindustry.graphics.g3d.*;
import mindustry.maps.planet.*;
import mindustry.mod.*;
import mindustry.type.*;
import mindustry.world.meta.*;
import randomMindustry.block.*;
import randomMindustry.item.*;
import randomMindustry.texture.*;

public class Main extends Mod {
    public static Planet random;
    public static TextureRegion arrival;
    
    public Main() {
        Events.on(ClientLoadEvent.class, e -> {
            Seq<TextureRegion> arrivals = new Seq<>();
            for(int i = 0; i < 7; i++) {
                arrivals.add(Core.atlas.find("random-mindustry-arrival" + i));
            }
            arrival = arrivals.get(0);
            Events.run(Trigger.update, () -> {
                int frame = (int)((Time.globalTime / 5f) % arrivals.size);
                arrival.set(arrivals.get(frame));
            });
        });
    }

    @Override
    public void init() {
        SeedManager.generateSeed();
        TextureManager.init();
        ItemMapper.editContent();
        BlockMapper.editContent();
        Vars.content.planets().select(p -> p != random).each(p -> p.hiddenItems.addAll(ItemMapper.generatedItems));

// debug atlas:
//        final int[] i = {0};
//        TextureManager.getAllTextures().each((t) -> {
//            PixmapIO.writePng(new Fi("atlas" + (i[0]++) + ".png"), t.getTextureData().getPixmap());
//        });

// debug packs:
        Log.info("DEBUG PACKS:");
        for (ItemPack pack : ItemMapper.packs) {
            Log.info(pack.tier + ":" + pack.localTier + " (" + pack.globalTier + ")");
            for (Item item : pack.all) {
                Log.info("      " + item.name + (pack.locked(item) ? " LOCKED" : ""));
            }
        }
    }

    @Override
    public void loadContent() {
        ItemMapper.generateContent();
        BlockMapper.generateContent();
        random = new Planet("random", Planets.sun, 1f, 3){{
            generator = new SerpuloPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 11, 0.15f, 0.13f, 5, new Color().set(Pal.spore).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                    new HexSkyMesh(this, 1, 0.6f, 0.16f, 5, Color.white.cpy().lerp(Pal.spore, 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
            );
            launchCapacityMultiplier = 0.5f;
            sectorSeed = 0;
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
                r.showSpawns = false;
            };
            atmosphereColor = Color.valueOf("3c1b8f");
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            startSector = 1;
            alwaysUnlocked = true;
            landCloudColor = Pal.spore.cpy().a(0.5f);
            hiddenItems.addAll(Vars.content.items()).removeAll(ItemMapper.generatedItems);
        }};
    }
}
