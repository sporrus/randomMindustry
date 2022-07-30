package randomMindustry.random.util;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;

import java.util.function.BiConsumer;

public class TextureGenerator {
    private static Pixmap pixmap;
    private static Texture newTexture;

    public static void createTexture() {
        newTexture = new Texture(pixmap);
        newTexture.setFilter(Texture.TextureFilter.linear, Texture.TextureFilter.linear);
    }

    public static void runForPixel(TextureRegion region, BiConsumer<Integer, Integer> consumer) {
        for (int y = 0; y < region.height; y++) {
            for (int x = 0; x < region.width; x++) {
                consumer.accept(x + region.getX(), y + region.getY());
            }
        }
    }

    public static TextureRegion changeHue(TextureRegion region, float hue) {
        pixmap = region.texture.getTextureData().consumePixmap();
        runForPixel(region, (x, y) -> {
            Color color = new Color();
            color.rgba8888(pixmap.get(x, y));
            color.hue(hue);
            pixmap.set(x, y, color);
        });
        createTexture();
        return new TextureRegion(newTexture, region.getX(), region.getY(), region.width, region.height);
    }
}
