package randomMindustry.ui.dialogs;

import arc.util.*;
import arc.math.*;
import arc.scene.ui.*;
import arc.scene.event.*;
import arc.scene.actions.*;
import mindustry.gen.*;
import mindustry.ui.dialogs.*;
import randomMindustry.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class LeverDialog extends BaseDialog{
    private Button button;
    
    public LeverDialog(){
        super("");
        button = new Button();
        
        shown(() -> button.touchable = Touchable.enabled);
        onResize(() -> button.actions(Actions.moveBy(0f, -50f, 0.01f, Interp.linear)));
        
        addCloseButton();
    }
    
    public void show(int num){
        cont.clear();
        
        cont.add("@rm-lever").row();
        
        button.clearListeners();
        button.label(() -> Integer.toString(num));
        cont.add(button).size(160f, 40f);
        button.clicked(() -> {
            Sounds.rockBreak.play();
            button.touchable = Touchable.disabled;
            button.actions(Actions.moveBy(0f, 50f, 0.125f, Interp.pow2In));
            Time.runTask(60f, () -> {
                hide();
                if(settings.getBool("rm-secret-menu", false)) return;
                if(Main.phase == num){
                    if(Main.phase >= 7){
                        settings.put("rm-secret-menu", true);
                        Sounds.corexplode.play();
                        ui.showInfo("@msg.rm-sequence-finish");
                        return;
                    }else{
                        Sounds.message.play();
                        ui.showInfo("@msg.rm-sequence-continue");
                        Main.phase++;
                        return;
                    }
                }else{
                    Sounds.explosionbig.play();
                    ui.showInfo("@msg.rm-sequence-fail");
                    Main.phase = 1;
                    return;
                }
            });
        });
        
        show();
    }
}
