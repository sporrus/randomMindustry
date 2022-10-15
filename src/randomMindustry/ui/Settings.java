package randomMindustry.ui;

import mindustry.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.*;
import randomMindustry.*;

public class Settings {
    public static void load() {
        SettingsMenuDialog dialog = Vars.ui.settings;

        dialog.addCategory("@setting.rm", Icon.effect, c -> {
            c.button("Regenerate", Main::reload).growX().expandX();
        });
    }
}
