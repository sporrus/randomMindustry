package randomMindustry.ui.dialogs;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.scene.style.*;
import arc.struct.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.util.*;
import mindustry.content.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import randomMindustry.*;

import static arc.Core.settings;
import static mindustry.Vars.*;

public class ItemFinderDialog extends BaseDialog {
    private TextField search = new TextField();
    private Table output = new Table();

    public ItemFinderDialog() {
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
        }).top().row();

        cont.pane(output).growX();
    }

    void rebuild() {
        output.clear();
        var text = search.getText();

        Seq<Item> items = content.items().select(i -> i.localizedName.toLowerCase().contains(text.toLowerCase()) && !i.hidden);
        Seq<Item> locked = new Seq<>();
        ResourceMapper.itemMap.each(pack -> locked.addAll(pack.locked));

        items.each(i -> {
            BaseDialog itemDialog = new BaseDialog("Info");
            Table table = displayContent(itemDialog, i);
            table.add("Recipes").color(Pal.accent).fillX();
            table.row();
            table.table(inset -> {
                inset.left();
                Seq<Block> ores = content.blocks().select(o -> o instanceof OreBlock && o.itemDrop == i);
                ores.each(b -> {
                    if (b instanceof OreBlock) {
                        TextureRegion image;
                        if (b.itemDrop.hardness == 1) image = UnitTypes.alpha.uiIcon;
                        else if (b.itemDrop.hardness == 2) image = Blocks.mechanicalDrill.uiIcon;
                        else if (b.itemDrop.hardness == 3) image = Blocks.pneumaticDrill.uiIcon;
                        else if (b.itemDrop.hardness == 4) image = Blocks.laserDrill.uiIcon;
                        else image = Blocks.blastDrill.uiIcon;

                        inset.image(image).size(30);
                        inset.image(Icon.right).padLeft(10).size(30);
                        inset.image(b.fullIcon).padLeft(10).size(30);
                        inset.image(Icon.right).padLeft(10).size(30);
                        inset.image(i.fullIcon).padLeft(10).size(30);
                        inset.row();
                    }
                });
                Seq<Block> recipes = findRecipe(i);
                recipes.each(b -> {
                    if (b instanceof GenericCrafter) {
                        inset.image(b.uiIcon).size(30);
                        inset.image(Icon.right).padLeft(10).size(30);
                        for(Consume consume : b.consumers){
                            if(consume instanceof ConsumeItems citems){
                                ItemStack[] itemStacks = citems.items;
                                for(ItemStack itemStack : itemStacks) inset.add(new ItemDisplay(itemStack.item, itemStack.amount, false)).padLeft(10);
                            }
                        }
                        inset.image(Icon.right).padLeft(10).size(30);
                        if (((GenericCrafter) b).outputItems != null) for(ItemStack itemStack : ((GenericCrafter)b).outputItems) inset.add(new ItemDisplay(itemStack.item, itemStack.amount, false)).padLeft(10);
                        if (((GenericCrafter) b).outputLiquids != null) for(LiquidStack liquidStack : ((GenericCrafter)b).outputLiquids) inset.add(new LiquidDisplay(liquidStack.liquid, liquidStack.amount, false)).padLeft(10);
                        inset.row();
                    }
                });
            }).fillX().padLeft(10);
            table.row();
            postContent(i, table);
            itemDialog.addCloseButton();

            boolean lock = locked.contains(i);
            output.table(t -> {
                t.button(i.localizedName, lock ? Icon.lock : new TextureRegionDrawable(i.uiIcon), itemDialog::show).color(lock ? Color.red : Color.white).growX();

                // add other finder stuff here
            }).expandX().fill(0.5f, 0).row();
        });

        if (output.getChildren().isEmpty()) {
            output.add("@none.found");
        }
    }

    public Seq<Block> findRecipe(Item item) {
        return content.blocks().select((block -> {
            if (block instanceof GenericCrafter) {
                ItemStack[] items = ((GenericCrafter) block).outputItems;
                if (items == null) return false;
                for (ItemStack itemStack : items) if (itemStack.item == item) return true;
                return false;
            }
            return false;
        }));
    }

    public Table displayContent(BaseDialog dialog, UnlockableContent content) {
        dialog.cont.clear();

        Table table = new Table();
        table.margin(10);

        //initialize stats if they haven't been yet
        content.checkStats();

        table.table(title1 -> {
            title1.image(content.uiIcon).size(iconXLarge).scaling(Scaling.fit);
            title1.add("[accent]" + content.localizedName + (settings.getBool("console") ? "\n[gray]" + content.name : "")).padLeft(5);
        });

        table.row();

        if (content.description != null) {
            var any = content.stats.toMap().size > 0;

            if (any) {
                table.add("@category.purpose").color(Pal.accent).fillX().padTop(10);
                table.row();
            }

            table.add("[lightgray]" + content.displayDescription()).wrap().fillX().padLeft(any ? 10 : 0).width(500f).padTop(any ? 0 : 10).left();
            table.row();

            if (!content.stats.useCategories && any) {
                table.add("@category.general").fillX().color(Pal.accent);
                table.row();
            }
        }

        Stats stats = content.stats;

        for (StatCat cat : stats.toMap().keys()) {
            OrderedMap<Stat, Seq<StatValue>> map = stats.toMap().get(cat);

            if (map.size == 0) continue;

            if (stats.useCategories) {
                table.add("@category." + cat.name).color(Pal.accent).fillX();
                table.row();
            }

            for (Stat stat : map.keys()) {
                table.table(inset -> {
                    inset.left();
                    inset.add("[lightgray]" + stat.localized() + ":[] ").left().top();
                    Seq<StatValue> arr = map.get(stat);
                    for (StatValue value : arr) {
                        value.display(inset);
                        inset.add().size(10f);
                    }

                }).fillX().padLeft(10);
                table.row();
            }
        }

        ScrollPane pane = new ScrollPane(table);
        dialog.cont.add(pane);
        return table;
    }

    public static void postContent(UnlockableContent content, Table table) {
        if (content.details != null) {
            table.add("[gray]" + (content.unlocked() || !content.hideDetails ? content.details : Iconc.lock + " " + Core.bundle.get("unlock.incampaign"))).pad(6).padTop(20).width(400f).wrap().fillX();
            table.row();
        }

        content.displayExtra(table);
    }
}
