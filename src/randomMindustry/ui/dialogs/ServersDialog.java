package randomMindustry.ui.dialogs;

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
        
        servers.each(s => {
            cont.button(s.name + "\n" + bundle.get("msg.rm-click-copy"), () -> {
                app.setClipboardText(s.ip);
                ui.showInfoFade("@copied");
            }).row();
        });
    }
    
    private static class RMServer{
        public String ip, name;
        
        public Server(ip, name){
            this.ip = ip;
            this.name = name;
        }
    }
}