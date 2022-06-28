package randomMindustry.ui.dialogs;

import mindustry.gen.*;
import mindustry.ui.dialogs.*;

import randomMindustry.ui.dialogs.*;

import static mindustry.Vars.*;

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
            
            cont.button("@rm-item-finder", Icon.zoom, this::itemFinder).row();
        }else{
            cont.defaults().size(130f).pad(5);
            
            cont.buttonRow("@rm-item-finder", Icon.zoom, this::itemFinder);
        }
    }
    
    void itemFinder(){
        ItemFinderDialog dialog = new ItemFinderDialog();
        dialog.show();
    }
}
