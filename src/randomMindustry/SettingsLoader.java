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
                    ui.loadfrag.show("@msg.rm-generating");
                    Time.runTask(120f, ui.loadfrag::hide);
                } catch (Exception ex) {
                    BaseDialog error = new BaseDialog("");
                    error.cont.add("[scarlet]Seed invalid!").row();
                    error.cont.add(ex.getMessage()).row();
                    error.buttons.button("@ok", error::hide).size(100f, 50f);
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
