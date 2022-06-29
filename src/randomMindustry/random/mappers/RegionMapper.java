package randomMindustry.random.mappers;

import arc.struct.*;
import arc.graphics.g2d.TextureAtlas.*;
import mindustry.*;
import randomMindustry.random.util.*;

import static arc.Core.*;

public class RegionMapper{
    public static Seq<AtlasRegion> oldRegions;

    public static void init(){
        if (Vars.headless) return;

        oldRegions = atlas.getRegions().copy();
        if(settings.getBool("rmchaos-region-swap", false)){
            Seq<AtlasRegion> regionsCopy = atlas.getRegions().copy();
            
            atlas.getRegions().each(region -> {
                AtlasRegion newRegion = regionsCopy.random(RandomUtil.getRand());
                regionsCopy.remove(newRegion);
                region.set(newRegion);
            });
        }
        
        /*if(settings.getBool("rmchaos-region-randsize", false)){
            atlas.getRegions().each(region -> {
                region.width = Mathf.random(0, region.width);
                region.height = Mathf.random(0, region.height);
            });
        }*/
    }
}
