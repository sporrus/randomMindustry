package randomMindustry.settings;

import arc.math.*;
import arc.flabel.*;
import arc.scene.style.Drawable;
import arc.scene.ui.*;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Planets;
import mindustry.gen.*;
import mindustry.type.Planet;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;
import mindustry.ui.dialogs.SettingsMenuDialog.*;
import mindustry.ui.dialogs.SettingsMenuDialog.SettingsTable.*;
import randomMindustry.*;
import randomMindustry.random.mappers.blocks.BlockMapper;
import randomMindustry.random.util.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class SettingsLoader {
    public static void init() {
        SettingsMenuDialog dialog = ui.settings;

        dialog.addCategory("@setting.rm", (Drawable) atlas.getDrawable("random-mindustry-dice"), c -> {
            c.areaTextPref("rm-seed", "0");
            c.pref(new TechSelector());
            c.pref(new RandomButton());
            c.pref(new GenerateButton());
        });

        dialog.addCategory("@setting.rmchaos", (Drawable) atlas.getDrawable("random-mindustry-dice"), c -> {
            c.pref(new SettingLabel("[accent]Very chaotic!"));
            c.checkPref("rmchaos-region-swap", false);
            c.checkPref("rmchaos-region-skullify", false);
            c.checkPref("rmchaos-router", false);
            c.checkPref("rmchaos-bundle-swap", false);
            c.checkPref("rmchaos-bundle-letter-swap", false);
            c.checkPref("rmchaos-category-rand", false);
            c.checkPref("rmchaos-region-scroller", false);
        });
    }

    static class TechSelector extends Setting {
        public TechSelector() {
            super("rm-tech");
        }

        @Override
        public void add(SettingsTable table) {
            table.table(Tex.button, t -> {
                t.margin(10f);
                var group = new ButtonGroup<>();
                var style = Styles.flatTogglet;

                t.defaults().size(140f, 50f);

                for (Planet planet : new Planet[]{Planets.serpulo, Planets.erekir}) {
                    t.button(planet.localizedName, style, () -> {
                        BlockMapper.setCurrentPlanet(planet);
                    }).group(group).checked((b) -> BlockMapper.getCurrentPlanet() == planet);
                }

                t.button("@rules.anyenv", style, () -> {
                    BlockMapper.setCurrentPlanet(null);
                }).group(group).checked((b) -> BlockMapper.getCurrentPlanet() == null);
            }).left().fill(false).expand(false, false).row();
        }
    }

    static class RandomButton extends Setting {
        public RandomButton() {
            super("rm-random");
            title = "setting.rm-random.name";
        }

        @Override
        public void add(SettingsTable table) {
            ImageButton button = table.button(Icon.refresh, () -> settings.put("rm-seed", Long.toString(new Rand().nextLong()))).get();
            button.label(() -> bundle.get(title));
            table.row();

            addDesc(button);
        }
    }

    static class GenerateButton extends Setting {
        public GenerateButton() {
            super("rm-generate");
            title = "setting.rm-generate.name";
        }

        @Override
        public void add(SettingsTable table) {
            ImageButton button = table.button(Icon.refresh, () -> {
                String seed = settings.getString("rm-seed");
                try {
                    RandomUtil.setSeed(Long.parseLong(seed));
                    Main.generate();
                } catch (Exception ex) {
                    BaseDialog error = new BaseDialog("");
                    error.cont.add(new FLabel("{wave}{shake}{wind}{sick}" + bundle.get("msg.rm-seed-invalid"))).row();
                    error.cont.add(ex.getMessage()).row();
                    error.buttons.button("@ok", error::hide).size(100f, 50f);
                    error.show();
                    Log.err(ex);
                    if (!Vars.headless) ui.loadfrag.hide();
                }
            }).get();
            button.label(() -> bundle.get(title));
            table.row();
            addDesc(button);
        }
    }
    
    static class SettingLabel extends Setting {
        public SettingLabel(String label) {
            super("rm-label");
            title = label;
        }
        
        @Override
        public void add(SettingsTable table) {
            Label text = table.add(title).get();
            table.row();
            addDesc(text);
        }
    }
}
