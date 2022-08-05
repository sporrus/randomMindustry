package randomMindustry.item;

import arc.graphics.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.type.*;
import randomMindustry.texture.*;

public class CustomItem extends Item {
    public CustomItem(String name, Color color) {
        super(name, color);
    }

    public void edit() {
        localizedName = "unreal item name";
        description = "unreal item description";
        explosiveness = 100;
        radioactivity = -1;
        flammability = 1;
        Item item = Vars.content.items().select(it -> !(it instanceof CustomItem)).random();
        float hue = Mathf.random(360f);
        fullIcon = TextureManager.alloc(item.fullIcon);
        uiIcon = TextureManager.alloc(item.uiIcon);
        TextureManager.hueRegion(fullIcon, hue);
        TextureManager.hueRegion(uiIcon, hue);
        color = color.cpy().hue(hue);
    }

    @Override
    public void displayExtra(Table t) {
        Label getReal = t.label(() -> "get real").get();
        t.row();
        
        getReal.actions(Actions.forever(Actions.moveBy(-1f, 0f)));
    }
}
