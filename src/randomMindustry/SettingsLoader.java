package randomMindustry;

import arc.*;
import arc.math.*;
import arc.util.*;
import arc.scene.ui.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;

import randomMindustry.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class SettingsLoader{
    public static void init(){
        SettingsMenuDialog dialog = ui.settings;
        
        dialog.addCategory("@setting.rm", Icon.effect /* TODO: Make custom icons. */, c -> {
            c.areaTextPref("rm-seed", "0");
            c.pref(new GenerateButton());
        });
    }
    
    static class GenerateButton extends Setting{
        public GenerateButton(){
            super("rm-generate");
            title = "setting.rm-generate.name";
        }
        
        @Override
        public void add(SettingsTable table){
            ImageButton button = table.button(Icon.refresh, () -> {
                String seed = settings.getString("rm-seed");
                try {
                    Main.rand = new Rand(Long.parseLong(seed));
                    Log.info(seed);
                    Log.info(Main.rand.seed0);
                    Log.info("we am insane");
                } catch (Exception ex) {
                    BaseDialog error = new BaseDialog("frog");
                    error.cont.add("[red]Seed invalid!").row();
                    error.cont.add(ex.getMessage());
                    error.cont.button("alr", error::hide).size(100f, 50f);
                    error.show();
                    return;
                }
                Main.generate();
            }).get();
            button.label(() -> bundle.get(title));
            table.row();
            
            addDesc(button);
        }
    }
}
