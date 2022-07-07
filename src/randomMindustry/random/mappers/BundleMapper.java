package randomMindustry.random.mappers;

import arc.graphics.g2d.TextureAtlas;
import arc.struct.*;
import mindustry.*;
import randomMindustry.random.util.RandomUtil;

import static arc.Core.*;
import static mindustry.Vars.*;

public class BundleMapper {
    public static void init(){
        if (headless) return;

        if(settings.getBool("rmchaos-bundle-swap", false)){
            ObjectMap<String, String> bundleCopy = bundle.getProperties().copy();

            bundle.getProperties().each((key, val) -> {
                String set = bundleCopy.get(bundleCopy.keys().toSeq().random(RandomUtil.getClientRand()));
                bundleCopy.put(key, set);
            });
            bundle.setProperties(bundleCopy);
            
            // hehe
            content.each(c -> {
                if(!(c instanceof UnlockableContent uc)) return;
                String bundleName = uc.getContentType + "." + uc.name;
                uc.localizedName = bundle.get(bundleName + ".name");
                uc.description = bundle.get(bundleName + ".description");
                uc.details = bundle.get(bundleName + ".details");
            });
        }

        if(settings.getBool("rmchaos-router", false)){
            bundle.debug("router");
            content.each(c -> {
                if(!(c instanceof UnlockableContent uc)) return;
                uc.localizedName = "router";
                uc.description = "router";
                uc.details = "router";
            });
        }
    }
}
