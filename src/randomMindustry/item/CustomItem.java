package randomMindustry.item;

import arc.flabel.*;
import arc.graphics.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;
import randomMindustry.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;

public class CustomItem extends Item {

    public CustomItem(String name, Color color) {
        super(name, color);
        hidden = false;
        alwaysUnlocked = true;
    }

    public void edit() {
        explosiveness = ItemMapper.r.random(1f);
        radioactivity = ItemMapper.r.random(1f);
        flammability = ItemMapper.r.random(1f);
        charge = ItemMapper.r.random(1f);
        Item item = Vars.content.items().select(it -> !(it instanceof CustomItem)).random(ItemMapper.r);
        float hue = ItemMapper.r.random(360f);
        fullIcon = TextureManager.alloc(item.fullIcon);
        uiIcon = TextureManager.alloc(item.uiIcon);
        TextureManager.hueRegion(fullIcon, hue);
        TextureManager.hueRegion(uiIcon, hue);
        color = color.cpy().hue(hue);
        localizedName = itemStringGen.generateName();
        description = itemStringGen.generateDescription(this);
        stats.add(RMVars.seed, RMVars.seedValue);
    }

    @Override
    public void displayExtra(Table t) {
        t.setBackground(Tex.button);
        t.image(Main.arrival).row();
        t.button("\"Special\" Stats", () -> {
            BaseDialog dialog = new BaseDialog("GET RICKROLLED!");
            dialog.cont.add(new FLabel("{wave}{sick}{wind}GET RICKROLLED!")).row();
            dialog.cont.image(Main.rickroll).row();
            dialog.addCloseButton();
            dialog.show();
        }).growX().row();
    }
}
