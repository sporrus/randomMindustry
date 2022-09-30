package randomMindustry;

import mindustry.world.meta.*;
import randomMindustry.string.*;

public class RMVars{
    public static ItemStringGenerator itemStringGen = new ItemStringGenerator();
    public static StatCat rmStats = new StatCat("rm");
    public static Stat
            seedStat = new Stat("rm-seed", rmStats),
            tierStat = new Stat("rm-tier", rmStats);
    public static StatValue seedStatValue = t -> t.add(Long.toString(SeedManager.getSeed()));
}
