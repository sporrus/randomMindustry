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
//        texture.setFilter(Texture.TextureFilter.linear, Texture.TextureFilter.linear);
        textures.add(texture);
    }

    public void checkTexture() {
        if (alloc >= count * count) {
            alloc = 0;
            newTexture();
        }
    }

    public TextureRegion alloc(Pixmap newPixmap) {
        return alloc(newPixmap, newPixmap.width, newPixmap.height);
    }

    public TextureRegion alloc(Pixmap newPixmap, int newWidth, int newHeight) {
        TextureRegion newRegion = alloc();
        if (newPixmap.width != newWidth || newPixmap.height != newHeight)
            newPixmap.draw(newPixmap, 0, 0, newWidth, newHeight);
        newRegion.texture.draw(newPixmap, newRegion.getX(), newRegion.getY());
        newRegion.texture.getTextureData().getPixmap().draw(newPixmap, newRegion.getX(), newRegion.getY());
        newRegion.setWidth(newPixmap.width);
        newRegion.setHeight(newPixmap.height);
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
