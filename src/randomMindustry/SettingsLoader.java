package randomMindustry;

import arc.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;

public class SettingsLoader{
    public static void init(){
        SettingsMenuDialog dialog = ui.settings;
        
        dialog.addCategory("@setting.rm", Icon.effect /* TODO: Make custom icons. */, c -> {
            c.textPref("rm-seed", "0");
            c.button("@setting.rm-regenerate", () -> {
                String seed = Core.settings.getString("rm-seed");
                try {
                    Main.rand = new Rand(Long.parseLong(seed));
                    Log.info(seed);
                    Log.info(Main.rand.seed0);
                    Log.info("i am insane");
                } catch (Exception ex) {
                    BaseDialog error = new BaseDialog("frog");
                    error.cont.add("[red]Seed invalid!").row();
                    error.cont.add(ex.getMessage());
                    error.cont.button("alr", error::hide).size(100f, 50f);
                    error.show();
                    return;
                }
                Main.generate();
            }).center().row();
        });
    }
}
