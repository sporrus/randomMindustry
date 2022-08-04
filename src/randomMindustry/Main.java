package randomMindustry;

import arc.graphics.*;
import arc.util.*;
import mindustry.mod.*;

public class Main extends Mod {
    public Main() {
    }

    @Override
    public void init() {
        TextureManager.init();
        ItemMapper.editContent();
    }

    @Override
    public void loadContent() {
        ItemMapper.generateContent();
    }
}
