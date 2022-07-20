package randomMindustry.ui.dialogs;

import mindustry.ui.dialogs.*;

import static arc.Core.*;
import static mindustry.Vars.*;

public class SecretMenuDialog extends BaseDialog{
    public SecretMenuDialog(){
        super("...");
        shouldPause = true;
        
        shown(this::rebuild);
        onResize(this::rebuild);
        
        addCloseButton();
    }
    
    void rebuild(){
        cont.clear();
        
        if(!mobile){
            cont.defaults().width(220f).height(55).pad(5f);
            cont.button(Icon.eye, () -> {
                if(!app.openURI("https://anuken.github.io")){
                    ui.showErrorMessage("@linkfail");
                    app.setClipboardText("https://anuken.github.io");
                }
            });
        }else{
            cont.defaults().size(130f).pad(5);
            cont.buttonRow("", Icon.eye, () -> {
                if(!app.openURI("https://anuken.github.io")){
                    ui.showErrorMessage("@linkfail");
                    app.setClipboardText("https://anuken.github.io");
                }
            });
        }
        
        cont.row();
        cont.add("@msg.rm-area-not-done").row();
    }
}