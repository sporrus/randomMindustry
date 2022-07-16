package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.content.Blocks;
import mindustry.core.UI;
import mindustry.ctype.*;
import mindustry.ui.Fonts;
import randomMindustry.random.util.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class BundleMapper {
    public static void init(){
        if (headless) return;

        if (settings.getBool("rmchaos-bundle-letter-swap", false)) {
            content.each(c -> {
                if(!(c instanceof UnlockableContent uc)) return;
                if (uc.localizedName != null) uc.localizedName = swap(uc.localizedName);
                if (uc.description != null) uc.description = swap(uc.description);
                if (uc.details != null) uc.details = swap(uc.details);
            });
        }

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
                String bundleName = uc.getContentType() + "." + uc.name;
                uc.localizedName = bundle.get(bundleName + ".name");
                uc.description = bundle.get(bundleName + ".description");
                uc.details = bundle.get(bundleName + ".details");
            });
        }

        if(settings.getBool("rmchaos-router", false)){
            ObjectMap<String, String> bundleCopy = bundle.getProperties().copy();
            bundle.getProperties().each((key, val) -> {
                StringBuilder routerString = new StringBuilder();
                for (int i = 0; i < val.length(); i++) {
                    if (val.charAt(i) != ' ') routerString.append(Blocks.router.emoji());
                    else routerString.append(val.charAt(i));
                }
                bundleCopy.put(key, routerString.toString());
            });
            bundle.setProperties(bundleCopy);
            content.each(c -> {
                if(!(c instanceof UnlockableContent uc)) return;
                uc.localizedName = "router";
                uc.description = "router";
                uc.details = "router";
            });
        }
    }

    public static String swap(String str) {
        String[] words = str.split(" +");
        for (int i = 0; i < words.length; i++) {
            int randomI = RandomUtil.getClientRand().nextInt(words.length);
            StringBuilder wordB = new StringBuilder(words[i]);
            StringBuilder newWordB = new StringBuilder(words[randomI]);
            char temp = wordB.charAt(0);
            wordB.setCharAt(0, newWordB.charAt(0));
            newWordB.setCharAt(0, temp);

            words[i] = wordB.toString();
            words[randomI] = newWordB.toString();
        }
        return String.join(" ", words);
    }
}
