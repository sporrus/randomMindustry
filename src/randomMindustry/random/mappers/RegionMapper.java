package randomMindustry.random.mappers;

import arc.math.*;
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
            Icon.icons.each((k, v) -> {
                AtlasRegion newRegion = atlas.getRegionMap().get("router");
                v.set(newRegion);
            });
        }

        if(settings.getBool("rmchaos-region-skullify", false)){
            atlas.getRegions().each(region -> {
                AtlasRegion newRegion = atlas.getRegionMap().get("random-mindustry-skull");
                newRegion.width = region.width;
                newRegion.height = region.height;
                region.set(newRegion);
            });
            Icon.icons.each((k, v) -> {
                AtlasRegion newRegion = atlas.getRegionMap().get("random-mindustry-skull");
                v.set(newRegion);
            });
        }
        
        if(settings.getBool("rmchaos-region-scroller", false)){
            Rand clientRand = RandomUtil.getClientRand();
            atlas.getRegions().each(region -> {
                region.setX(clientRand.random(0f, 500f));
                region.setY(clientRand.random(0f, 500f));
                region.setWidth(clientRand.random(30f, 50f));
                region.setHeight(clientRand.random(30f, 50f));
            });
            /* Icon.icons.each((k, v) -> {
                v.setX(clientRand.random(0f, 500f));
                v.setY(clientRand.random(0f, 500f));
            }); */
        }
    }
}
