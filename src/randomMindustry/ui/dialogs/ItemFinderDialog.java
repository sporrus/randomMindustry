package randomMindustry.ui.dialogs;

import arc.struct.*; 
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.dialogs.*;

import static mindustry.Vars.*;

public class ItemFinderDialog extends BaseDialog{
    private TextField search = new TextField();
    private Table output = new Table();
    
    public ItemFinderDialog(){
        super("@rm-item-finder");
        shouldPause = true;
        
        shown(this::rebuild);
        onResize(this::rebuild);
        
        addCloseButton();
        
        // definitely didn't take this from DatabaseDialog.java
        cont.table(s -> {
            s.image(Icon.zoom).padRight(8);
            search = s.field(null, text -> rebuild()).growX().get();
            search.setMessageText("@players.search");
        }).row();
        
        cont.pane(output);
    }
    
    void rebuild(){
        output.clear();
        var text = search.getText();
        
        Seq<Item> items = content.items().select(i -> i.localizedName.toLowerCase().contains(text.toLowerCase()) && !i.hidden);
        
        items.each(i -> {
            output.table(t -> {
                t.setBackground(Tex.button);
                
                t.add(i.localizedName).row();
                
                // add other finder stuff here
            }).row();
        });
        
        if(output.getChildren().isEmpty()){
            output.add("@none.found");
        }
    }
}
