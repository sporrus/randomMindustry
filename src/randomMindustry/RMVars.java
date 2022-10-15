package randomMindustry;

import arc.struct.*;
import mindustry.world.meta.*;
import randomMindustry.random.*;
import randomMindustry.string.*;
import randomMindustry.texture.*;

public class RMVars{
    public static ItemStringGenerator itemStringGen = new ItemStringGenerator();
    public static StatCat rmStats = new StatCat("rm");
    public static Stat
            seedStat = new Stat("rm-seed", rmStats),
            tierStat = new Stat("rm-tier", rmStats);
    public static StatValue seedStatValue = t -> t.add(Long.toString(SeedManager.getSeed()));

    public static int itemSpriteX = 8;
    public static int itemSpriteY = 3;
    public static int itemSprites = itemSpriteX * itemSpriteY;

    public static ObjectMap<Integer, TextureGrid> crafterSprites = ObjectMap.of(
            2, new TextureGrid("random-mindustry-crafters2"),
            3, new TextureGrid("random-mindustry-crafters3")
    );
    public static ObjectMap<Integer, TextureGrid> wallSprites = ObjectMap.of(
            1, new TextureGrid("random-mindustry-walls1"),
            2, new TextureGrid("random-mindustry-walls2")
    );
    public static TextureGrid oreSprites = new TextureGrid("random-mindustry-ores");
    public static TextureGrid drillSprites = new TextureGrid("random-mindustry-drills");
    public static TextureGrid routerSprites = new TextureGrid("random-mindustry-routers");
    public static TextureGrid bridgeSprites = new TextureGrid("random-mindustry-bridges");
}
