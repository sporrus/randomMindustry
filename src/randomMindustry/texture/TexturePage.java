package randomMindustry.texture;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.graphics.gl.*;
import arc.struct.*;

public class TexturePage {
    public Seq<Texture> textures = new Seq<>();
    public Texture texture;
    public int alloc = 0;
    public int size;
    public int count;

    public TexturePage(int size) {
        this.size = size;
        this.count = 2048 / size;
        newTexture();
    }

    public void newTexture() {
        texture = new Texture(new PixmapTextureData(new Pixmap(size * count, size * count), false, false));
        texture.setFilter(Texture.TextureFilter.linear, Texture.TextureFilter.linear);
        textures.add(texture);
    }

    public void checkTexture() {
        if (alloc >= count * count) {
            alloc = 0;
            newTexture();
        }
    }

    public TextureRegion alloc(TextureRegion oldRegion) {
        return alloc(oldRegion, oldRegion.width, oldRegion.height);
    }

    public TextureRegion alloc(TextureRegion oldRegion, int newWidth, int newHeight) {
        TextureRegion newRegion = alloc();
        Pixmap newPixmap = oldRegion.texture.getTextureData().getPixmap().crop(oldRegion.getX(), oldRegion.getY(), oldRegion.width, oldRegion.height);
        if (oldRegion.width != newWidth || oldRegion.height != newHeight)
            newPixmap.draw(newPixmap, 0, 0, newWidth, newHeight);
        newRegion.texture.draw(newPixmap, newRegion.getX(), newRegion.getY());
        newRegion.texture.getTextureData().getPixmap().draw(newPixmap, newRegion.getX(), newRegion.getY());
        newRegion.setWidth(oldRegion.width);
        newRegion.setHeight(oldRegion.height);
        newPixmap.dispose();
        return newRegion;
    }

    public TextureRegion alloc() {
        checkTexture();
        int x = alloc % count;
        int y = alloc / count;
        alloc++;
        return new TextureRegion(texture, x * size, y * size, size, size);
    }

    public void dispose() {
        for (Texture texture : textures) {
            texture.getTextureData().consumePixmap().dispose();
            texture.dispose();
        }
        textures.clear();
    }
}
