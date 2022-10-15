package randomMindustry.texture;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import mindustry.graphics.*;

public class TextureGrid {
    public String packerName;

    public TextureGrid(String packerName) {
        this.packerName = packerName;
    }

    public Pixmap getPacker(MultiPacker packer) {
        return packer.get(packerName).crop();
    }

    public Pixmap get() {
        TextureRegion region = Core.atlas.find(packerName);
        return region.texture.getTextureData().getPixmap().crop(region.getX(), region.getY(), region.width, region.height);
    }

    public Pixmap random(Pixmap region, int width, int height, Rand r) {
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

    public Pixmap random(MultiPacker packer, int width, int height, Rand r) {
        return random(getPacker(packer), width, height, r);
    }

    public Pixmap random(int size, Rand r) {
        return random(size, size, r);
    }

    public Pixmap random(int width, int height, Rand r) {
        return random(get(), width, height, r);
    }
}
