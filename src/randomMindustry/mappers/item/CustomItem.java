package randomMindustry.mappers.item;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Tex;
import mindustry.type.Item;
import randomMindustry.RMVars;
import randomMindustry.texture.TextureManager;

import static randomMindustry.RMVars.itemStringGen;
import static randomMindustry.mappers.item.ItemMapper.r;

public class CustomItem extends Item {
    public float hue = 0;
    public ItemPack tier;

    public CustomItem(String name, Color color) {
        super(name, color);
        hidden = false;
        alwaysUnlocked = true;
    }

    public void edit() {
        tier = ItemMapper.getPack(this);
        explosiveness = r.random(1f);
        radioactivity = r.random(1f);
        flammability = r.random(1f);
        charge = r.random(1f);

        Item item = Vars.content.items().select(it -> !(it instanceof CustomItem)).random(r);
        float hue = r.random(360f);
        fullIcon = TextureManager.alloc(item.fullIcon);
        uiIcon = TextureManager.alloc(item.uiIcon);
        TextureManager.hueRegion(fullIcon, hue);
        TextureManager.hueRegion(uiIcon, hue);
        color = color.cpy().hue(hue);
        this.hue = hue;

        localizedName = itemStringGen.generateName();
        description = itemStringGen.generateDescription(this);

        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(RMVars.tierStat, t -> t.add(tier.tier + " " + tier.globalTier + " (" + tier.localTier + ")"));
    }
}
