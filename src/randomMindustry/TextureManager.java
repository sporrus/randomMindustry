package randomMindustry;

import arc.*;
import arc.files.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.*;

public class TextureManager {
    public static Texture itemTexture;
    public static int itemAlloc = 0;
    public static int itemWidth = 64;
    public static int itemHeight = 64;
    public static Seq<Texture> textures = new Seq<>();

    public static void init() {
        Events.on(EventType.DisposeEvent.class, (e) -> {
            for (Texture texture : textures) {
                texture.getTextureData().consumePixmap().dispose();
                texture.dispose();
            }
            textures.clear();
        });
        newItemTexture();
    }

    public static void updateTexture() {
        itemTexture.load(new PixmapTextureData(itemTexture.getTextureData().getPixmap(), false, false));
    }

    public static void newItemTexture() {
        itemTexture = new Texture(new PixmapTextureData(new Pixmap(itemWidth * 32, itemHeight * 32), false, false));
        itemTexture.setFilter(Texture.TextureFilter.linear, Texture.TextureFilter.linear);
        textures.add(itemTexture);
    }

    public static void checkItemTexture() {
        if (itemAlloc >= itemWidth * itemHeight) {
            itemAlloc = 0;
            newItemTexture();
        }
    }

    public static TextureRegion allocItem() {
        checkItemTexture();
        int x = itemAlloc % itemWidth;
        int y = itemAlloc / itemWidth;
        itemAlloc++;
        return new TextureRegion(itemTexture, x * 32, y * 32, 32, 32);
    }

    static int u = 0;

    public static TextureRegion allocItem(TextureRegion oldRegion) {
        TextureRegion newRegion = allocItem();
        Pixmap newPixmap = oldRegion.texture.getTextureData().getPixmap().crop(oldRegion.getX(), oldRegion.getY(), oldRegion.width, oldRegion.height);
        newRegion.texture.getTextureData().getPixmap().draw(newPixmap, newRegion.getX(), newRegion.getY());
        newRegion.setWidth(oldRegion.width);
        newRegion.setHeight(oldRegion.height);
        newPixmap.dispose();
        updateTexture();
        return newRegion;
    }

    public static void hueRegion(TextureRegion region, float hue) {
        Pixmap pixmap = region.texture.getTextureData().getPixmap().crop(region.getX(), region.getY(), region.width, region.height);
        Color color = new Color();
        pixmap.each((x, y) -> {
            color.rgba8888(pixmap.get(x, y));
            color.hue(hue);
            pixmap.set(x, y, color);
        });
        region.texture.getTextureData().getPixmap().draw(pixmap, region.getX(), region.getY());
        updateTexture();
        pixmap.dispose();
    }
}
