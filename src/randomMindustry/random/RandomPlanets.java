package randomMindustry.random;

import arc.graphics.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.block.blocks.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomPlanets {
    public static Planet random, star;

    public static void load() {
        star = new Planet("rm-star", Planets.sun, 4f){{
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

        random = new Planet("rm-random", star, 1f, 3){{
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
            defaultCore = (RandomCore) BlockMapper.generatedBlocks.find(b -> b instanceof RandomCore c && c.id == 0);
            defaultLoadout = RandomLoadouts.loadouts.first();
            landCloudColor = Team.crux.color.cpy().a(0.5f);
            hiddenItems.addAll(Vars.content.items()).removeAll(ItemMapper.generatedItems);
        }};
    }
}
