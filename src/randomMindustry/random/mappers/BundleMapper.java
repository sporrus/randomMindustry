package randomMindustry.random.mappers;

import arc.graphics.g2d.TextureAtlas;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.Vars;
import randomMindustry.random.util.RandomUtil;

import static arc.Core.*;

public class BundleMapper {
    public static void init(){
        if (Vars.headless) return;

        if(settings.getBool("rmchaos-region-swap", false)){
            Seq<TextureAtlas.AtlasRegion> regionsCopy = atlas.getRegions().copy();

            atlas.getRegions().each(region -> {
                TextureAtlas.AtlasRegion newRegion = regionsCopy.random(RandomUtil.getRand());
                regionsCopy.remove(newRegion);
                region.set(newRegion);
            });
        }

        if(settings.getBool("rmchaos-router", false)){
            ObjectMap<String, String> properties = bundle.getProperties();
            properties.each((key, value) -> properties.put(key, "router"));
            bundle.setProperties(properties);
        }

        /*if(settings.getBool("rmchaos-region-randsize", false)){
            atlas.getRegions().each(region -> {
                region.width = Mathf.random(0, region.width);
                region.height = Mathf.random(0, region.height);
            });
        }*/
    }
}
