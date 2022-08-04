package randomMindustry;

import mindustry.mod.*;
import randomMindustry.block.*;
import randomMindustry.item.*;
import randomMindustry.texture.*;

public class Main extends Mod {
    public Main() {
    }

    @Override
    public void init() {
        SeedManager.generateSeed();
        TextureManager.init();
        ItemMapper.editContent();
        BlockMapper.editContent();
//        final int[] i = {0};
//        TextureManager.getAllTextures().each((t) -> {
//            PixmapIO.writePng(new Fi("atlas" + (i[0]++) + ".png"), t.getTextureData().getPixmap());
//        });
    }

    @Override
    public void loadContent() {
        ItemMapper.generateContent();
        BlockMapper.generateContent();
    }
}
