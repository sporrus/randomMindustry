package randomMindustry.texture;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.graphics.*;

public class TextureGrid {
    public ObjectMap<Integer, TextureCount> sizes;
    public String packerName;

    // sizes must be [size, xCount, yCount, ...]
    public TextureGrid(String packerName, int... sizes) {
        if (sizes.length % 3 != 0) throw new RuntimeException("Sizes must be % 3");
        this.sizes = new ObjectMap<>();
        for (int i = 0; i < sizes.length; i+=3)
            this.sizes.put(sizes[i], new TextureCount(sizes[i + 1], sizes[i + 2]));
        this.packerName = packerName;
    }

    public PixmapRegion get(MultiPacker packer, int size) {
        if (!sizes.containsKey(size)) throw new RuntimeException("No atlas for size " + size);
        if (!packer.has(packerName + size)) throw new RuntimeException("No texture " + packerName + " in packer");
        return packer.get(packerName + size);
    }

    public Pixmap random(MultiPacker packer, int size, Rand r) {
        PixmapRegion region = get(packer, size);
        TextureCount tCount = sizes.get(size);
        int sprite = r.random(0, tCount.count - 1);
        int x = (sprite % tCount.x) * size;
        int y = (sprite / tCount.x) * size;
        return region.crop(x, y, size, size);
    }

    public static class TextureCount {
        public int x;
        public int y;
        public int count;

        public TextureCount(int x, int y) {
            this.x = x;
            this.y = y;
            this.count = x * y;
        }
    }
}
