package randomMindustry.random.mappers;

import arc.Core;
import arc.struct.*;
import arc.graphics.g2d.TextureAtlas.*;
import mindustry.*;
import mindustry.gen.Icon;
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
                AtlasRegion newRegion = regionsCopy.random(RandomUtil.getClientRand());
                regionsCopy.remove(newRegion);
                newRegion.width = region.width;
                newRegion.height = region.height;
                region.set(newRegion);
            });
        }

        if(settings.getBool("rmchaos-router", false)){
            atlas.getRegions().each(region -> {
                AtlasRegion newRegion = atlas.getRegionMap().get("router");
                newRegion.width = region.width;
                newRegion.height = region.height;
                region.set(newRegion);
            });
            Icon.icons.values().forEach((t) -> {
                AtlasRegion newRegion = atlas.getRegionMap().get("router");
                t.set(newRegion);
            });
        }

        if(settings.getBool("rmchaos-region-skullify", false)){
            atlas.getRegions().each(region -> {
                AtlasRegion newRegion = atlas.getRegionMap().get("random-mindustry-skull");
                newRegion.width = region.width;
                newRegion.height = region.height;
                region.set(newRegion);
            });
            Icon.icons.values().forEach((t) -> {
                AtlasRegion newRegion = atlas.getRegionMap().get("random-mindustry-skull");
                t.set(newRegion);
            });
            
        }
    }
}
