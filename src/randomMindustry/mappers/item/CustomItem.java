package randomMindustry.mappers.item;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import mindustry.Vars;
import mindustry.gen.Tex;
import mindustry.graphics.MultiPacker;
import mindustry.type.Item;
import randomMindustry.RMVars;
import randomMindustry.texture.TextureManager;

import static randomMindustry.RMVars.itemStringGen;
import static randomMindustry.mappers.item.ItemMapper.r;

public class CustomItem extends Item {
    public float hue;

    public CustomItem(String name) {
        super(name, Color.red.cpy().saturation(0.75f));

        explosiveness = r.random(1f);
        radioactivity = r.random(1f);
        flammability = r.random(1f);
        charge = r.random(1f);

        localizedName = itemStringGen.generateName();
        description = itemStringGen.generateDescription(this);
        hue = r.random(360f);
        color = color.cpy().hue(hue);

        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(RMVars.tierStat, t -> {
            ItemPack tier = ItemMapper.getPack(this);
            t.add(tier.tier + " " + tier.globalTier + " (" + tier.localTier + ")");
        });
    }

    @Override
    public void loadIcon() {
        int sprite = r.random(0, RMVars.itemSprites);
        TextureRegion region = Core.atlas.find("random-mindustry-item" + sprite);
        fullIcon = uiIcon = TextureManager.alloc(region);
        TextureManager.hueRegion(fullIcon, hue);
    }
}
