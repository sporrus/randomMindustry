package randomMindustry;

import arc.graphics.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;

public class ItemMapper {
    public static final Seq<Item> generatedItems = new Seq<>();
    public static final int itemCount = 16;

    public static void generateContent() {
        for (int i = 0; i < itemCount; i++) {
            Item item = new Item("random-item-" + i, Color.red);
            generatedItems.add(item);
        }
    }

    public static void editContent() {
        generatedItems.each(i -> {
            i.localizedName = "gorodmi made this item (real)";
            i.explosiveness = 1;
            Item item = Mathf.chance(0.01) ? Items.beryllium : Items.blastCompound;
            i.fullIcon = TextureManager.allocItem(item.fullIcon);
            i.uiIcon = TextureManager.allocItem(item.uiIcon);
        });
    }
}
