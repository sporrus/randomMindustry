package randomMindustry.settings;

import arc.math.*;
import arc.flabel.*;
import arc.scene.ui.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;
import randomMindustry.*;
import randomMindustry.random.util.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class SettingsLoader{
    public static void init(){
        SettingsMenuDialog dialog = ui.settings;
        
        dialog.addCategory("@setting.rm", Icon.effect /* TODO: Make custom icons. */, c -> {
            c.areaTextPref("rm-seed", "0");
            c.pref(new RandomButton());
            c.pref(new GenerateButton());
        });
        
        dialog.addCategory("@setting.rmchaos", Icon.effect /* TODO: Make custom icons. */, c -> {
            c.checkPref("rmchaos-region-swap", false);
             c.checkPref("rmchaos-region-skullify", false);
            c.checkPref("rmchaos-category-rand", false);
        });
    }
    
    static class RandomButton extends Setting{
        public RandomButton(){
            super("rm-random");
            title = "setting.rm-random.name";
        }
        
        @Override
        public void add(SettingsTable table){
            ImageButton button = table.button(Icon.refresh, () -> settings.put("rm-seed", Long.toString(new Rand().nextLong()))).get();
            button.label(() -> bundle.get(title));
            table.row();
            
            addDesc(button);
        }
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
                    RandomUtil.setSeed(Long.parseLong(seed));
                    ui.loadfrag.show("@msg.rm-generating");
                    Main.generate();
                    ui.loadfrag.hide();
                } catch (Exception ex) {
                    BaseDialog error = new BaseDialog("");
                    error.cont.add(new FLabel("{wave}{shake}{wind}{sick}" + bundle.get("msg.rm-seed-invalid"))).row();
                    error.cont.add(ex.getMessage()).row();
                    error.buttons.button("@ok", error::hide).size(100f, 50f);
                    error.show();
                }
            }).get();
            button.label(() -> bundle.get(title));
            table.row();
            addDesc(button);
        }
    }
}
