package randomMindustry.ui.dialogs;

import arc.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ctype.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.ui.dialogs.*;
import mindustry.world.meta.*;

import static arc.Core.*;
import static mindustry.Vars.*;

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
        cont.add(pane).growX().expandX();
    }

    public void postContent(UnlockableContent content) {
        if (content.details != null) {
            contentTable.add("[gray]" + (content.unlocked() || !content.hideDetails ? content.details : Iconc.lock + " " + Core.bundle.get("unlock.incampaign"))).pad(6).padTop(20).width(400f).wrap().fillX();
            contentTable.row();
        }

        content.displayExtra(contentTable);
    }
}
