package randomMindustry.ui.dialogs;

import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.ItemDisplay;
import mindustry.ui.LiquidDisplay;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.meta.BuildVisibility;
import randomMindustry.util.Util;

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
