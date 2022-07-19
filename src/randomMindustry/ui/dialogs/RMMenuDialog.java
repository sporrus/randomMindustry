package randomMindustry.ui.dialogs;

import arc.Events;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import arc.util.Time;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.ui.Fonts;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;

public class RMMenuDialog extends BaseDialog{
    private static final TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(Styles.cleart);
    private static final TextButton.TextButtonStyle mobileStyle = new TextButton.TextButtonStyle(Tex.buttonEdge4, Tex.buttonEdgeOver4, Tex.buttonEdge4, Fonts.def);

    public RMMenuDialog(){
        super("@rm-menu");
        shouldPause = true;

        style.up = Tex.clear;
        style.down = Styles.flatDown;
        
        shown(this::rebuild);
        onResize(this::rebuild);
        
        addCloseButton();
    }
    
    void rebuild(){
        cont.clear();

        if(!mobile){
            cont.defaults().width(220f).height(55).pad(5f);
            cont.button("@rm-item-finder", Icon.zoom, Dialogs.itemFinderDialog::show).row();
            cont.button("@rm-servers", Icon.host, Dialogs.serversDialog::show).row();
        }else{
            cont.defaults().size(130f).pad(5);
            cont.buttonRow("@rm-item-finder", Icon.zoom, Dialogs.itemFinderDialog::show);
            cont.buttonRow("@rm-servers", Icon.host, Dialogs.serversDialog::show);
        }
    }

    public void addPauseButton() {
        ui.paused.buttons.button("@rm-menu", Icon.effect, this::show).width(220f).height(55).pad(5f).row();
    }

    public void desktopMenuButton() {
        Table buttons = ui.menuGroup.find("buttons");
        buttons.button("@rm-menu", Icon.effect, style, this::show).marginLeft(11);
    }

    public void addMenuButton() {
        if (!mobile) {
            // death
            Time.run(20, () -> {
                desktopMenuButton();
                Events.on(EventType.ResizeEvent.class, (e) -> desktopMenuButton());
            });
        } else {
            Table buttons = ui.menuGroup.find("buttons");
            buttons.button("@rm-menu", Icon.effect, mobileStyle, this::show).top().left().grow().size(84, 45).get();
        }
    }
}
