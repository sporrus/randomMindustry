package randomMindustry.texture;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.game.*;
import randomMindustry.random.*;

import static mindustry.Vars.*;

public class TextureManager {
    public static Color teamDark = Color.valueOf("9e8080ff");
    public static Color teamMid = Color.valueOf("dbc5c5ff");
    public static Color teamLight = Color.valueOf("ffffffff");
    public static final ObjectMap<Integer, TexturePage> pages = new ObjectMap<>();

    public static void init() {
        Events.on(EventType.DisposeEvent.class, (e) -> pages.each((k, v) -> v.dispose()));
    }

    public static TexturePage getOrCreatePage(int size) {
        if (pages.containsKey(size)) return pages.get(size);
        pages.put(size, new TexturePage(size));
        return pages.get(size);
    }

    public static TextureRegion alloc(Pixmap newPixmap, int newWidth, int newHeight) {
        return getOrCreatePage((int) Math.ceil(newWidth / 32f) * 32).alloc(newPixmap, newWidth, newHeight);
    }

    public static TextureRegion alloc(Pixmap newPixmap) {
        return alloc(newPixmap, newPixmap.width, newPixmap.height);
    }

    public static void reload() {
        pages.each((k, v) -> v.reload());
    }

    public static Seq<Texture> getAllTextures() {
        Seq<Texture> seq = new Seq<>();
        pages.values().toSeq().each(p -> seq.addAll(p.textures));
        return seq;
    }

    public static void recolorRegion(Pixmap pixmap, Color newColor) {
        Color newDark = new Color(newColor.r * teamDark.r / teamMid.r, newColor.g * teamDark.g / teamMid.g, newColor.b * teamDark.b / teamMid.b);
        Color newLight = new Color(newColor.r * teamLight.r / teamMid.r, newColor.g * teamLight.g / teamMid.g, newColor.b * teamLight.b / teamMid.b);
        Color color = new Color();
        pixmap.each((x, y) -> {
            color.rgba8888(pixmap.get(x, y));
            if (color.equals(teamDark)) color.set(newDark);
            else if (color.equals(teamMid)) color.set(newColor);
            else if (color.equals(teamLight)) color.set(newLight);
            pixmap.set(x, y, color);
        });
    }

    public static void shadow(Pixmap pixmap) {
        Pixmap shadow = pixmap.crop(0, 0, pixmap.width, pixmap.height);
        int offset = pixmap.width / tilesize - 1;
        int shadowColor = Color.rgba8888(0, 0, 0, 0.3f);
        for(int x = 0; x < pixmap.width; x++)
            for(int y = offset; y < pixmap.height; y++)
                if(shadow.getA(x, y) == 0 && shadow.getA(x, y - offset) != 0)
                    pixmap.set(x, y, shadowColor);
    }
}
