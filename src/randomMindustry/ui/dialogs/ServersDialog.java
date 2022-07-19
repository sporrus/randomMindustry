package randomMindustry.ui.dialogs;

import arc.struct.Seq;
import arc.util.Http;
import arc.util.Log;
import arc.util.serialization.JsonReader;
import arc.util.serialization.JsonValue;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.app;
import static arc.Core.bundle;
import static mindustry.Vars.ui;

public class ServersDialog extends BaseDialog{
    private final JsonReader json = new JsonReader();
    private Seq<RMServerCategory> serverCategories = new Seq<>();
    
    public ServersDialog(){
        super("@rm-servers");
        shouldPause = true;
        
        shown(this::rebuild);
        onResize(this::rebuild);
        
        addCloseButton();

        Http.HttpRequest request = Http.get("https://raw.githubusercontent.com/sporrus/randomMindustry/master/servers.json");
        request.submit((response) -> {
            if (response.getStatus() == Http.HttpStatus.OK) {
                JsonValue root = json.parse(response.getResultAsString());
                for (int i = 0; i < root.size; i++) {
                    JsonValue serverCategory = root.get(i);
                    Seq<RMServer> servers = new Seq<>();
                    JsonValue serverArray = serverCategory.get("servers");
                    for (int j = 0; j < serverArray.size; j++) {
                        JsonValue server = serverArray.get(i);
                        servers.add(new RMServer(server.getString("address"), server.getString("name"), server.getString("version"), server.getString("description")));
                    }
                    serverCategories.add(new RMServerCategory(serverCategory.getString("name"), servers));
                }
                Log.info("loaded " + serverCategories.size + " categories");
            } else {
                Log.warn("cant get any servers");
            }
        });
    }
    
    void rebuild(){
        cont.clear();

        cont.pane(list -> {
            serverCategories.each(sc -> {
                list.label(() -> sc.name).growX().row();
                sc.servers.each(s -> {
                    list.table((t) -> {
                        t.label(() -> s.name + "\n" + s.description).fill(0.25f, 0).expandX();
                        t.label(() -> s.version + "\n" + s.address).fill(0.25f, 0).expandX();
                        t.button(bundle.get("msg.rm-click-copy"), () -> {
                            app.setClipboardText(s.address);
                            ui.showInfoFade("@copied");
                        }).fill(0.5f, 0).expandX();
                    }).growX().row();
                });
            });
        }).growX();
    }
    
    private static class RMServer{
        public String address, name, version, description;

        public RMServer(String address, String name, String version, String description) {
            this.address = address;
            this.name = name;
            this.version = version;
            this.description = description;
        }
    }

    private static class RMServerCategory{
        public String name;
        public Seq<RMServer> servers;

        public RMServerCategory(String name, Seq<RMServer> servers) {
            this.name = name;
            this.servers = servers;
        }
    }
}
