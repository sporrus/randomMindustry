package randomMindustry.random.mappers;

import arc.struct.*;
import mindustry.*;

import static arc.Core.*;

public class BundleMapper {
    public static void init(){
        if (Vars.headless) return;

        if(settings.getBool("rmchaos-router", false)){
            ObjectMap<String, String> properties = bundle.getProperties();
            properties.each((key, value) -> properties.put(key, "router"));
            bundle.setProperties(properties);
        }
    }
}
