package randomMindustry.random;

import arc.graphics.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.game.*;
import mindustry.graphics.g3d.*;
import mindustry.type.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.block.blocks.*;
import randomMindustry.mappers.item.*;



public class RandomPlanets {
    public static final SyncedRand r = new SyncedRand();
    
    /* There's probably a better way... */
    public static int planetSeed = 0;
    
    public static Planet random, star;

    public static void load() {
        star = new Planet("rm-star", Planets.sun, 4f){{
            bloom = true;
            accessible = false;
            orbitRadius = 500f;
            drawOrbit = false;
            
            Color lastColor = RandomUtil.genColor(r);

            meshLoader = () -> new SunMesh(
                this, 4,
                5, 0.3, 1.7, 1.2, 1,
                1.1f,
                lastColor.shiftHue(r.random(-2f, 2f)).cpy(),
                lastColor.shiftHue(r.random(-2f, 2f)).cpy(),
                lastColor.shiftHue(r.random(-2f, 2f)).cpy(),
                lastColor.shiftHue(r.random(-2f, 2f)).cpy(),
                lastColor.shiftHue(r.random(-2f, 2f)).cpy(),
                lastColor.shiftHue(r.random(-2f, 2f)).cpy()
            );
        }};

        random = new Planet("rm-random", star, 1f, 3){{
            localizedName = "Random";
            generator = new RandomPlanetGenerator(){{
                defaultLoadout = RandomLoadouts.loadouts.first();
            }};
            meshLoader = () -> new HexMesh(this, 6);
            
            Color lastColor = RandomUtil.genColor(r);
            
            cloudMeshLoader = () -> new MultiMesh(
                new HexSkyMesh(this, r.random(1, Integer.MAX_VALUE), 0.15f, 0.13f, 5, lastColor.shiftHue(r.random(-10f, 10f)).cpy().mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                new HexSkyMesh(this, r.random(1, Integer.MAX_VALUE), 0.6f, 0.16f, 5, Color.white.cpy().lerp(lastColor.shiftHue(r.random(-10f, 10f)).cpy(), 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
            );
            launchCapacityMultiplier = 0.5f;
            sectorSeed = planetSeed = r.random(1, Integer.MAX_VALUE);
            allowWaves = true;
            allowWaveSimulation = true;
            allowSectorInvasion = true;
            allowLaunchSchematics = true;
            enemyCoreSpawnReplace = true;
            allowLaunchLoadout = true;
            Team def = genTeam(Team.derelict);
            Team wave = genTeam(def);
            ruleSetter = r -> {
                r.defaultTeam = def;
                r.waveTeam = wave;
                r.placeRangeCheck = false;
                r.attributes.clear();
                r.showSpawns = true;
            };
            atmosphereColor = RandomUtil.genColor(r).a(0.65f);
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            startSector = 1;
            alwaysUnlocked = true;
            defaultCore = (RandomCore)BlockMapper.generatedBlocks.find(b -> b instanceof RandomCore c && c.id == 0);
            landCloudColor = atmosphereColor.cpy().a(0.5f);
            hiddenItems.addAll(Vars.content.items()).removeAll(ItemMapper.generatedItems);
            solarSystem = star;
        }};
    }
    
    // needs better version
    public static void reload(){
        /*star.meshLoader = () -> new SunMesh(
            this, 4,
            5, 0.3, 1.7, 1.2, 1,
            1.1f,
            RandomUtil.genColor(r),
            RandomUtil.genColor(r),
            RandomUtil.genColor(r),
            RandomUtil.genColor(r),
            RandomUtil.genColor(r),
            RandomUtil.genColor(r)
        );
        
        random.cloudMeshLoader = () -> new MultiMesh(
            new HexSkyMesh(this, r.random(1, Integer.MAX_VALUE), 0.15f, 0.13f, 5, RandomUtil.genColor(r).mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
            new HexSkyMesh(this, r.random(1, Integer.MAX_VALUE), 0.6f, 0.16f, 5, Color.white.cpy().lerp(RandomUtil.genColor(r), 0.55f).a(0.75f), 2, 0.45f, 1f, 0.41f)
        );*/
        Team def = genTeam(Team.derelict);
        Team wave = genTeam(def);
        random.ruleSetter = r -> {
            r.defaultTeam = def;
            r.waveTeam = wave;
            r.placeRangeCheck = false;
            r.attributes.clear();
            r.showSpawns = true;
        };
        random.sectorSeed = planetSeed = r.random(1, Integer.MAX_VALUE);
        random.atmosphereColor = RandomUtil.genColor(r).a(0.65f);
        random.landCloudColor = random.atmosphereColor.cpy().a(0.5f);
    }
    
    public static Team genTeam(Team except){
        Seq<Team> teams = new Seq<>(Team.all).select(t -> t != except && t != Team.derelict);
        return teams.random(r);
    }
}
