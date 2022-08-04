package randomMindustry.texture;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.math.*;
import arc.struct.*;
import mindustry.game.*;

public class TextureManager {
    public static final ObjectMap<Integer, TexturePage> pages = new ObjectMap<>();

    public static void init() {
        Events.on(EventType.DisposeEvent.class, (e) ->
                pages.values().toSeq().each(TexturePage::dispose));
    }

    public static TexturePage getOrCreatePage(int size) {
        if (pages.containsKey(size)) return pages.get(size);
        pages.put(size, new TexturePage(size));
        return pages.get(size);
    }

    public static TextureRegion alloc(TextureRegion oldRegion) {
        return getOrCreatePage((int) Math.ceil(oldRegion.width / 32f) * 32).alloc(oldRegion);
    }

    public static Seq<Texture> getAllTextures() {
        Seq<Texture> seq = new Seq<>();
        pages.values().toSeq().each(p -> seq.addAll(p.textures));
        return seq;
    }

    public static void hueRegion(TextureRegion region, float hue) {
        Pixmap pixmap = region.texture.getTextureData().getPixmap().crop(region.getX(), region.getY(), region.width, region.height);
        Color color = new Color();
        pixmap.each((x, y) -> {
            color.rgba8888(pixmap.get(x, y));
            color.hue(hue);
            pixmap.set(x, y, color);
        });
        region.texture.draw(pixmap, region.getX(), region.getY());
        region.texture.getTextureData().getPixmap().draw(pixmap, region.getX(), region.getY());
        pixmap.dispose();
    }
}
