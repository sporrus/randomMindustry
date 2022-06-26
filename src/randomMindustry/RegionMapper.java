package randomMindustry;

import arc.struct.*;
import arc.graphics.g2d.*;
import arc.graphics.g2d.TextureAtlas.*;
import arc.math.*;

import static arc.Core.*;

public class RegionMapper{
    public static void init(){
        if(settings.getBool("rmchaos-region-swap", false)){
            Seq<AtlasRegion> regionsCopy = atlas.getRegions().copy();
            
            atlas.getRegions().each(region -> {
                AtlasRegion newRegion = regionsCopy.random(Main.rand);
                regionsCopy.remove(newRegion);
                region.set(newRegion);
            });
        }
        
        if(settings.getBool("rmchaos-region-randsize", false)){
            atlas.getRegions().each(region -> {
                region.width = Mathf.random(0, region.width);
                region.height = Mathf.random(0, region.height);
            });
        }
    }
}
