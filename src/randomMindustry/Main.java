package randomMindustry;

import arc.util.*;
import mindustry.mod.*;

public class Main extends Mod {
    public static int phase = 1;

    public Main() {
        Log.info("uh");
    }

    @Override
    public void init() {
        TextureManager.newItemTexture();
        ItemMapper.editContent();
    }

    @Override
    public void loadContent() {
        ItemMapper.generateContent();
    }
}
