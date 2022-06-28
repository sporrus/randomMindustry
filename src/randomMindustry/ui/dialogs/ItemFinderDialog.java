package randomMindustry.ui.dialogs;

import arc.struct.*; 
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;

import randomMindustry.*;

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
                
                content.blocks().select(b -> {
                    if(!(b instanceof GenericCrafter crafter)) return false;
                    ItemStack[] itemStacks = crafter.outputItems;
                    if(itemStacks == null) return false;
                    for(ItemStack itemStack : itemStacks) if (itemStack.item.localizedName.equalsIgnoreCase(i.localizedName)) return true;
                    return true;
                }).each(b -> {
                    t.image(b.uiIcon).size(40);
                    t.image(Icon.right).size(40);
                    for(Consume consume : b.consumers){
                        if(consume instanceof ConsumeItems citems){
                            ItemStack[] itemStacks = citems.items;
                            for(ItemStack itemStack : itemStacks) t.add(new ItemDisplay(itemStack.item, itemStack.amount, false));
                        }
                    }
                    t.image(Icon.right).size(40);
                    for(ItemStack itemStack : ((GenericCrafter)b).outputItems) t.add(new ItemDisplay(itemStack.item, itemStack.amount, false));
                    t.row();
                    
                    // display if drillable by "hand"
                });
            }).row();
        });
        
        if(output.getChildren().isEmpty()){
            output.add("@none.found");
        }
    }
}
