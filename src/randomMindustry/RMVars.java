package randomMindustry;

import mindustry.world.meta.*;
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

    public static TextureGrid crafterSprites = new TextureGrid("random-mindustry-crafters", 64,11,1, 96,1,1);
}
