package randomMindustry;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;

public class TextureManager {
    public static Texture itemTexture;
    public static int itemAlloc = 0;
    public static Seq<Texture> textures = new Seq<>();

    public static void newItemTexture() {
        itemTexture = new Texture(64 * 32, 64 * 32);
        itemTexture.setFilter(Texture.TextureFilter.linear, Texture.TextureFilter.linear);
        textures.add(itemTexture);
    }

    public static void checkItemTexture() {
        if (itemAlloc >= 64 * 64) {
            itemAlloc = 0;
            newItemTexture();
        }
    }

    public static TextureRegion allocItem() {
        checkItemTexture();
        int x = itemAlloc % 64;
        int y = itemAlloc / 64;
        itemAlloc++;
        return new TextureRegion(itemTexture, x * 32, y * 32, 32, 32);
    }

    public static TextureRegion allocItem(TextureRegion oldRegion) {
        TextureRegion newRegion = allocItem();
        Pixmap oldPixmap = oldRegion.texture.getTextureData().getPixmap();
        Pixmap newPixmap = new Pixmap(oldRegion.width, oldRegion.height);
        Color color = new Color();
        for (int y = 0; y < oldRegion.height; y++) {
            for (int x = 0; x < oldRegion.width; x++) {
                color.rgba8888(oldPixmap.get(x + oldRegion.getX(), y + oldRegion.getY()));
                newPixmap.set(x, y, color);
            }
        }
        newRegion.texture.draw(newPixmap, newRegion.getX(), newRegion.getY());
        newRegion.setWidth(oldRegion.width);
        newRegion.setHeight(oldRegion.height);
        return newRegion;
    }
}
