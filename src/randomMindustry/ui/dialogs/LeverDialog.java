package randomMindustry.ui.dialogs;

import arc.util.*;
import arc.scene.ui.*;
import arc.scene.event.*;
import arc.scene.actions.*;
import mindustry.ui.dialogs.*;

public class LeverDialog extends BaseDialog{
    private Button button;
    
    public LeverDialog(){
        super("");
        
        button = new Button();
        
        button.clicked(() -> {
            button.touchable = Touchable.disabled;
            button.actions(Actions.moveBy(0f, 50f, 0.25f, Interp.linear));
            Time.runTask(60f, this::hide);
        });
        
        shown(() -> button.actions(Actions.moveBy(0f, -50f, 0.01f, Interp.linear)));
        onResize(() -> button.actions(Actions.moveBy(0f, -50f, 0.01f, Interp.linear)));
    }
    
    public show(int num){
        cont.clear();
        
        cont.add("@rm-lever");
        
        button.label(() -> Integer.toString(num));
        cont.add(button).size(160f, 40f);
        
        show();
    }
}
