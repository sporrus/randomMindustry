package randomMindustry.ui;

import arc.*;
import arc.scene.ui.*;
import arc.util.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;
import randomMindustry.*;
import randomMindustry.random.*;

import java.text.*;

public class Settings {
    public static void load() {
        SettingsMenuDialog dialog = Vars.ui.settings;

        dialog.addCategory("@setting.rm", Icon.effect, c -> {
            c.areaTextPref("rm-seed", String.valueOf(SeedManager.getSeed()));
            c.pref(new Setting("rm-generate") {
                {title = "setting.rm-generate.name";}
                @Override
                public void add(SettingsMenuDialog.SettingsTable table) {
                    ImageButton button = table.button(Icon.effect, () -> {
                        String seed = Core.settings.getString("rm-seed");
                        try {
                            SeedManager.setSeed(Long.parseLong(seed));
                            Main.reload();
                        } catch (NumberFormatException e) {
                            Vars.ui.showException("Cannot parse seed " + seed, e);
                        }
                    }).growX().expandX().get();
                    button.label(() -> Core.bundle.get(title)).align(Align.right);
                    table.row();
                    addDesc(button);
                }
            });
        });
    }
}
