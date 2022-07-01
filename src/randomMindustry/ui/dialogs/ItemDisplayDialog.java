package randomMindustry.ui.dialogs;

import arc.graphics.*;
import arc.struct.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import randomMindustry.util.*;

public class ItemDisplayDialog extends ContentDisplayDialog {
    @Override
    public void midContent(UnlockableContent content) {
        contentTable.add("Recipes").color(Pal.accent).fillX();
        contentTable.row();
        contentTable.table(inset -> {
            inset.left();
            Seq<Block> blocks = Util.findHowToGet((Item) content);
            blocks.each(block -> {
                if (block instanceof GenericCrafter genericCrafter) {
                    inset.image(block.uiIcon).size(30);
                    inset.image(Icon.right).padLeft(10).size(30);
                    for (Consume consume : block.consumers) {
                        if (consume instanceof ConsumeItems citems) {
                            ItemStack[] itemStacks = citems.items;
                            for (ItemStack itemStack : itemStacks)
                                inset.add(new ItemDisplay(itemStack.item, itemStack.amount, false)).padLeft(10);
                        }
                    }
                    inset.image(Icon.right).padLeft(10).size(30);
                    if ((genericCrafter).outputItems != null) for (ItemStack itemStack : (genericCrafter).outputItems)
                        inset.add(new ItemDisplay(itemStack.item, itemStack.amount, false)).padLeft(10);
                    if ((genericCrafter).outputLiquids != null)
                        for (LiquidStack liquidStack : (genericCrafter).outputLiquids)
                            inset.add(new LiquidDisplay(liquidStack.liquid, liquidStack.amount, false)).padLeft(10);
                } else {
                    inset.image(Util.getHardnessIcon(block)).size(30);
                    inset.image(Icon.right).padLeft(10).size(30);
                    inset.image(block.uiIcon).padLeft(10).size(30);
                    inset.image(Icon.right).padLeft(10).size(30);
                    inset.image(content.uiIcon).padLeft(10).size(30);
                }
                inset.row();
            });
        }).fillX().padLeft(10);
        contentTable.row();
        contentTable.add("Purpose").color(Pal.accent).fillX();
        contentTable.row();
        contentTable.table(inset -> {
            inset.left();
            Seq<Block> blocks = Util.findRecipeIn((Item) content);
            blocks.each(block -> {
                if (block.buildVisibility != BuildVisibility.shown) return;
                if (new Seq<>(block.requirements).contains(itemStack -> itemStack.item == content)) {
                    inset.image(block.uiIcon).size(30);
                    inset.image(Icon.hammer).padLeft(10).padRight(10).size(30);
                    for (ItemStack itemStack : block.requirements) inset.add(new ItemDisplay(itemStack.item, itemStack.amount, false));
                    inset.row();
                }
                if (new Seq<>(block.consumers).contains((consume -> consume instanceof ConsumeItems consumeItems &&
                        new Seq<>(consumeItems.items).contains((itemStack -> itemStack.item == content))))) {
                    inset.image(block.uiIcon).size(30);
                    inset.image(Icon.left).color(Color.yellow).padLeft(10).padRight(10).size(30);
                    for (Consume consume : block.consumers){
                        if (!(consume instanceof ConsumeItems consumeItem)) continue;
                        for (ItemStack itemStack : consumeItem.items) inset.add(new ItemDisplay(itemStack.item, itemStack.amount, false));
                    }
                    inset.row();
                }
            });
        }).fillX().padLeft(10);
        contentTable.row();
    }
}
