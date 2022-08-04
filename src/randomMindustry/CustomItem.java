package randomMindustry;

import arc.graphics.*;
import arc.scene.ui.layout.*;
import mindustry.type.*;

public class CustomItem extends Item {
    public CustomItem(String name, Color color) {
        super(name, color);
    }

    @Override
    public void displayExtra(Table t) {
        t.label(() -> "get real").row();
    }
}
