package randomMindustry.ui.dialogs;

import mindustry.gen.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;

public class RMMenuDialog extends BaseDialog{
    public RMMenuDialog(){
        super("@dialog.rm-menu");
        shouldPause = true;
        
        shown(this::rebuild);
        
        addCloseButton();
    }
    
    void rebuild(){
        if(!mobile){
            cont.defaults().width(220f).height(55).pad(5f);
            
            cont.button("@rm-credits", Icon.info, () -> {}).row();
            cont.button("@rm-item-finder", Icon.search, () -> {}).row();
        }else{
            cont.defaults().size(130f).pad(5);
            
            cont.buttonRow("@rm-credits", Icon.info, () -> {}).row();
            cont.buttonRow("@rm-item-finder", Icon.search, () -> {}).row();
        }
    }
}
