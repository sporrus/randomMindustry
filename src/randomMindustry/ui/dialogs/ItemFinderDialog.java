package randomMindustry.ui.dialogs;

import arc.graphics.*;
import arc.scene.style.*;
import arc.struct.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;
import randomMindustry.random.mappers.*;

import static mindustry.Vars.*;

public class ItemFinderDialog extends BaseDialog {
    private TextField search = new TextField();
    private Table output = new Table();

    public ItemFinderDialog() {
        super("@rm-item-finder");
        shouldPause = true;

        shown(this::rebuild);
        onResize(this::rebuild);

        addCloseButton();

        // definitely didn't take this from DatabaseDialog.java
        cont.table(s -> {
            s.image(Icon.zoom).padRight(8);
            search = s.field(null, text -> rebuild()).growX().get();
            // todo: uhhh @players.search??? change it
            search.setMessageText("@players.search");
        }).top().row();

        cont.pane(output).growX();
    }

    void rebuild() {
        output.clear();
        var text = search.getText();

        Seq<Item> items = content.items().select(i -> i.localizedName.toLowerCase().contains(text.toLowerCase()) && !i.hidden);
        Seq<Item> locked = new Seq<>();
        ResourceMapper.itemMap.each(pack -> locked.addAll(pack.locked));

        items.each(i -> {
            boolean lock = locked.contains(i);
            output.table(t -> {
                t.button(i.localizedName, lock ? Icon.lock : new TextureRegionDrawable(i.uiIcon), () -> Dialogs.itemDialog.show(i)).color(lock ? Color.red : Color.white).growX();
            }).expandX().fill(0.5f, 0).row();
        });

        if (output.getChildren().isEmpty()) {
            output.add("@none.found");
        }
    }
}
