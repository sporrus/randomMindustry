package randomMindustry.item;

import arc.graphics.*;
import arc.math.*;
import arc.scene.actions.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.*;
import mindustry.type.*;
import randomMindustry.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;

public class CustomItem extends Item {

    public CustomItem(String name, Color color) {
        super(name, color);
    }

    public void edit() {
        localizedName = itemStringGen.generateName();
        description = itemStringGen.generateDescription(this);
        explosiveness = ItemMapper.r.rand.random(1f);
        radioactivity = ItemMapper.r.rand.random(1f);
        flammability = ItemMapper.r.rand.random(1f);
        charge = ItemMapper.r.rand.random(1f);
        Item item = Vars.content.items().select(it -> !(it instanceof CustomItem)).random();
        float hue = ItemMapper.r.rand.random(360f);
        fullIcon = TextureManager.alloc(item.fullIcon);
        uiIcon = TextureManager.alloc(item.uiIcon);
        TextureManager.hueRegion(fullIcon, hue);
        TextureManager.hueRegion(uiIcon, hue);
        color = color.cpy().hue(hue);
    }

    @Override
    public void displayExtra(Table t) {
        Image image = t.image(Main.arrival).get();
        t.row();
        
        image.actions(Actions.forever(Actions.rotateBy(1f)));
    }
}
