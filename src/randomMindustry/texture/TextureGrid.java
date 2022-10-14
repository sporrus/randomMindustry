package randomMindustry.texture;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.graphics.*;
import randomMindustry.*;

public class TextureGrid {
    public String packerName;

    public TextureGrid(String packerName) {
        this.packerName = packerName;
    }

    public PixmapRegion get(MultiPacker packer) {
        return packer.get(packerName);
    }

    public Pixmap random(MultiPacker packer, int width, int height, Rand r) {
        PixmapRegion region = get(packer);
        int xSize = region.width / width;
        int ySize = region.height / height;
        int sprite = r.random(0, xSize * ySize - 1);
        int x = (sprite % xSize) * width;
        int y = (sprite / xSize) * height;
        return region.crop(x, y, width, height);
    }

    public Pixmap random(MultiPacker packer, int size, Rand r) {
        return random(packer, size, size, r);
    }
}
