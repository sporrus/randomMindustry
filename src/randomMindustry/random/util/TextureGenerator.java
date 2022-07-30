package randomMindustry.random.util;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;

import java.util.function.BiConsumer;

public class TextureGenerator {
    private static Pixmap pixmap;
    private static Texture oldTexture;

    public static void runForPixel(TextureRegion region, BiConsumer<Integer, Integer> consumer) {
        for (int y = 0; y < region.height; y++) {
            for (int x = 0; x < region.width; x++) {
                consumer.accept(x, y);
            }
        }
    }

    public static void changeHue(TextureRegion region, float hue) {
        if (oldTexture != region.texture) {
            pixmap = region.texture.getTextureData().consumePixmap();
            oldTexture = region.texture;
        }
        Pixmap newPixmap = new Pixmap(region.width, region.height);
        runForPixel(region, (x, y) -> {
            Color color = new Color();
            color.rgba8888(pixmap.get(x + region.getX(), y + region.getY()));
            color.hue(hue);
            newPixmap.set(x, y, color);
        });
        oldTexture.draw(newPixmap, region.getX(), region.getY());
    }
}
