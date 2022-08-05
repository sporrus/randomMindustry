package randomMindustry;

import arc.scene.ui.layout.*;
import mindustry.world.meta.*;
import randomMindustry.string.*;

public class RMVars{
    public static ItemStringGenerator itemStringGen = new ItemStringGenerator();
    public static Stat seed = new Stat("rm-seed");
    public static StatValue seedValue = t -> t.add(Long.toString(SeedManager.getSeed()));
}
