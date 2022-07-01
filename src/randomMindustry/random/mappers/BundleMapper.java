package randomMindustry.random.mappers;

import arc.graphics.g2d.TextureAtlas;
import arc.struct.*;
import mindustry.*;
import randomMindustry.random.util.RandomUtil;

import static arc.Core.*;

public class BundleMapper {
    public static void init(){
        if (Vars.headless) return;

        if(settings.getBool("rmchaos-bundle-swap", false)){
            ObjectMap<String, String> bundleCopy = bundle.getProperties().copy();

            bundle.getProperties().each((key, val) -> {
                String set = bundleCopy.get(bundleCopy.keys().toSeq().random(RandomUtil.getClientRand()));
                bundleCopy.put(key, set);
            });
            bundle.setProperties(bundleCopy);
        }

        if(settings.getBool("rmchaos-router", false)){
            ObjectMap<String, String> properties = bundle.getProperties();
            properties.each((key, value) -> properties.put(key, "router"));
            bundle.setProperties(properties);
        }
    }
}
