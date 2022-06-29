package randomMindustry.ui.dialogs;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import arc.struct.OrderedMap;
import arc.struct.Seq;
import arc.util.Scaling;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.gen.Iconc;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.ui.ItemDisplay;
import mindustry.ui.LiquidDisplay;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.consumers.Consume;
import mindustry.world.consumers.ConsumeItems;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;
import mindustry.world.meta.StatValue;
import mindustry.world.meta.Stats;
import randomMindustry.util.Util;

import static arc.Core.settings;
import static mindustry.Vars.content;
import static mindustry.Vars.iconXLarge;

public abstract class ContentDisplayDialog extends BaseDialog {
    public Table contentTable;

    public ContentDisplayDialog() {
        super("Info");
        addCloseButton();
    }

    public void show(UnlockableContent content) {
        displayContent(content);
        midContent(content);
        postContent(content);
        show();
    }

    public abstract void midContent(UnlockableContent content);

    public void displayContent(UnlockableContent content) {
        cont.clear();

        contentTable = new Table();
        contentTable.margin(10);

        //initialize stats if they haven't been yet
        content.checkStats();

        contentTable.table(title1 -> {
            title1.image(content.uiIcon).size(iconXLarge).scaling(Scaling.fit);
            title1.add("[accent]" + content.localizedName + (settings.getBool("console") ? "\n[gray]" + content.name : "")).padLeft(5);
        });

        contentTable.row();

        if (content.description != null) {
            var any = content.stats.toMap().size > 0;

            if (any) {
                contentTable.add("@category.purpose").color(Pal.accent).fillX().padTop(10);
                contentTable.row();
            }

            contentTable.add("[lightgray]" + content.displayDescription()).wrap().fillX().padLeft(any ? 10 : 0).width(500f).padTop(any ? 0 : 10).left();
            contentTable.row();

            if (!content.stats.useCategories && any) {
                contentTable.add("@category.general").fillX().color(Pal.accent);
                contentTable.row();
            }
        }

        Stats stats = content.stats;

        for (StatCat cat : stats.toMap().keys()) {
            OrderedMap<Stat, Seq<StatValue>> map = stats.toMap().get(cat);

            if (map.size == 0) continue;

            if (stats.useCategories) {
                contentTable.add("@category." + cat.name).color(Pal.accent).fillX();
                contentTable.row();
            }

            for (Stat stat : map.keys()) {
                contentTable.table(inset -> {
                    inset.left();
                    inset.add("[lightgray]" + stat.localized() + ":[] ").left().top();
                    Seq<StatValue> arr = map.get(stat);
                    for (StatValue value : arr) {
                        value.display(inset);
                        inset.add().size(10f);
                    }

                }).fillX().padLeft(10);
                contentTable.row();
            }
        }

        ScrollPane pane = new ScrollPane(contentTable);
        cont.add(pane);
    }

    public void postContent(UnlockableContent content) {
        if (content.details != null) {
            contentTable.add("[gray]" + (content.unlocked() || !content.hideDetails ? content.details : Iconc.lock + " " + Core.bundle.get("unlock.incampaign"))).pad(6).padTop(20).width(400f).wrap().fillX();
            contentTable.row();
        }

        content.displayExtra(contentTable);
    }
}
