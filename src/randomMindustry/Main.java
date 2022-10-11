package randomMindustry;

import arc.graphics.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.mod.*;
import mindustry.type.*;
import randomMindustry.mappers.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

public class Main extends Mod {
    public static Planet random;
    
    public Main() {

    }

    @Override
    public void init() {
        TextureManager.init();
        Vars.content.planets().select(p -> p != random).each(p -> p.hiddenItems.addAll(ItemMapper.generatedItems));

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
    public void loadContent() {
        SeedManager.generateSeed();
        Mappers.item.generateContent();
        Mappers.block.generateContent();
        random = new Planet("rm-random", Planets.sun, 1f, 3){{
            localizedName = "Random";
            generator = new RandomPlanetGenerator();
            meshLoader = () -> new HexMesh(this, 6);
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 11, 0.15f, 0.13f, 5, new Color().set(Team.crux.color.cpy()).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                    new HexSkyMesh(this, 1, 0.6f, 0.16f, 5, Color.white.cpy().lerp(Team.crux.color.cpy(), 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
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
                r.showSpawns = true;
            };
            atmosphereColor = Team.crux.color.cpy().a(0.65f);
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            startSector = 1;
            alwaysUnlocked = true;
            landCloudColor = Team.crux.color.cpy().a(0.5f);
            hiddenItems.addAll(Vars.content.items()).removeAll(ItemMapper.generatedItems);
        }};
        RandomTechTree.load();
    }
}
