package randomMindustry.ui.dialogs;

import arc.struct.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;
import static arc.Core.*;

public class ServersDialog extends BaseDialog{
    private Seq<RMServer> servers = new Seq<>();
    
    public ServersDialog(){
        super("@rm-servers");
        shouldPause = true;
        
        shown(this::rebuild);
        onResize(this::rebuild);
        
        addCloseButton();
        
        servers.add(new RMServer("n3.yeet.ml:6572", "Yeet Server"));
    }
    
    void rebuild(){
        cont.clear();
        
        cont.defaults().width(220f).height(55).pad(5f);
        
        cont.pane(list -> {
            servers.each(s -> {
                list.button(s.name + "\n" + bundle.get("msg.rm-click-copy"), () -> {
                    app.setClipboardText(s.ip);
                    ui.showInfoFade("@copied");
                }).growX().row();
            });
        }).growX();
    }
    
    private static class RMServer{
        public String ip, name;
        
        public RMServer(String ip, String name){
            this.ip = ip;
            this.name = name;
        }
    }
}
