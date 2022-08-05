package randomMindustry;

import arc.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import arc.util.*;
import mindustry.game.EventType.*;
import mindustry.mod.*;
import mindustry.ui.dialogs.*;
import randomMindustry.block.*;
import randomMindustry.item.*;
import randomMindustry.texture.*;

public class Main extends Mod {
    public static TextureRegion arrival;
    
    public Main() {
        Events.on(ClientLoadEvent.class, e -> {
            Seq<TextureRegion> arrivals = new Seq<>();
            for(int i = 0; i < 7; i++) {
                arrivals.add(Core.atlas.find("random-mindustry-arrival" + i));
            }
            arrival = arrivals.get(0);
            Events.run(Trigger.update, () -> {
                int frame = (int)((Time.globalTime / 5f) % arrivals.size);
                arrival.set(arrivals.get(frame));
            });
        });
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
