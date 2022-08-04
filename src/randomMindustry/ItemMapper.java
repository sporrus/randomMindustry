package randomMindustry;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.*;
import mindustry.type.*;

public class ItemMapper {
    public static final Seq<Item> generatedItems = new Seq<>();
    public static final int itemCount = 32;

    public static void generateContent() {
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = new CustomItem("random-item-" + i, Color.red);
            generatedItems.add(item);
        }
    }

    public static void editContent() {
        generatedItems.each(i -> {
            i.localizedName = "fake item";
            i.explosiveness = 100;
            i.radioactivity = -1;
            i.flammability = 1;
            Item item = Vars.content.items().select(it -> !(it instanceof CustomItem)).random();
            float hue = Mathf.random(360f);
            i.fullIcon = TextureManager.allocItem(item.fullIcon);
            TextureManager.hueRegion(i.fullIcon, hue);
            i.uiIcon = TextureManager.allocItem(item.uiIcon);
            TextureManager.hueRegion(i.uiIcon, hue);
            i.color = i.color.cpy().hue(hue);
        });
    }
}
