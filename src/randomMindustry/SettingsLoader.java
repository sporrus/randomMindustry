package randomMindustry;

import mindustry.gen.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;

public class SettingsLoader{
    public static void init(){
        SettingsMenuDialog dialog = ui.settings;
        
        dialog.addCategory("@setting.rm", Icon.effect /* TODO: Make custom icons. */, c -> {
            // TODO: Add settings.
        });
    }
}
