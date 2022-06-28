package randomMindustry.ui.dialogs;

import mindustry.gen.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;

public class RMMenuDialog extends BaseDialog{
    public RMMenuDialog(){
        super("@dialog.rm-menu");
        shouldPause = true;
        
        shown(this::rebuild);
        onResize(this::rebuild);
        
        addCloseButton();
    }
    
    void rebuild(){
        cont.clear();

        if(!mobile){
            cont.defaults().width(220f).height(55).pad(5f);
            
            cont.button("@rm-item-finder", Icon.zoom, () -> {}).row();
        }else{
            cont.defaults().size(130f).pad(5);
            
            cont.buttonRow("@rm-item-finder", Icon.zoom, () -> {});
        }
    }
}
