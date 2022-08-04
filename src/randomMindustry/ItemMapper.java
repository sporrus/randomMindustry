package randomMindustry;

import arc.files.*;
import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

public class ItemMapper {
    public static final Seq<Item> generatedItems = new Seq<>();
    public static final int itemCount = 16;

    public static void generateContent() {
        for (int i = 0; i < itemCount; i++) {
            CustomItem item = new CustomItem("random-item-" + i, Color.red);
            generatedItems.add(item);
        }
    }

    public static void editContent() {
        generatedItems.each(i -> {
            i.localizedName = "gorodmi made this item (real)";
            i.explosiveness = 1;
            Item item = Vars.content.items().select(it -> !(it instanceof CustomItem)).random();
            float hue = Mathf.random(360f);
            i.stats.add(new Stat("gorodmi") {
                @Override
                public String localized() {
                    return "real";
                }
            }, "top 10 gorodmi:\n10) gorodmi\n9) gorodmi\n8) gorodmi\n7) gorodmi\n6) gorodmi\n5) gorodmi\n4) gorodmi\n3) gorodmi\n2) gorodmi\n1) gorodmi");
            i.fullIcon = TextureManager.allocItem(item.fullIcon);
            TextureManager.hueRegion(i.fullIcon, hue);
            i.uiIcon = TextureManager.allocItem(item.uiIcon);
            TextureManager.hueRegion(i.uiIcon, hue);
            i.color = i.color.cpy().hue(hue);
        });
        PixmapIO.writePng(new Fi("uh.png"), TextureManager.itemTexture.getTextureData().getPixmap());
    }
}
