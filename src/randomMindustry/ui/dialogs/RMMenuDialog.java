package randomMindustry.ui.dialogs;

import arc.math.*;
import arc.util.*;
import arc.scene.ui.*;
import arc.scene.event.*;
import arc.scene.actions.*;
import mindustry.gen.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class RMMenuDialog extends BaseDialog{
    public RMMenuDialog(){
        super("@rm-menu");
        shouldPause = true;
        
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
            if(settings.getBool("rm-book-collected", false)) cont.button("@rm-book", Icon.book, () -> ui.showInfo("@msg.rm-lever-hunt")).row();
        }else{
            cont.defaults().size(130f).pad(5);
            cont.buttonRow("@rm-item-finder", Icon.zoom, Dialogs.itemFinderDialog::show);
            cont.buttonRow("@rm-servers", Icon.host, Dialogs.serversDialog::show);
            if(settings.getBool("rm-book-collected", false)){
                cont.row();
                cont.buttonRow("@rm-book", Icon.book, () -> ui.showInfo("@msg.rm-lever-hunt"));
            }
        }
        
        if(!settings.getBool("rm-book-collected", false)){
            ImageButton book = buttons.button(Icon.book, Styles.emptyi, () -> {}).bottom().left().size(40f).pad(5f).get();
            book.clicked(() -> {
                settings.put("rm-book-collected", true);
                book.actions(Actions.moveBy(0f, -50f, 0.5f, Interp.pow2In));
                Time.runTask(30f, this::rebuild);
            });
        }
    }
}
