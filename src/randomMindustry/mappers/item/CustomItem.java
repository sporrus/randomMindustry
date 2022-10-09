package randomMindustry.mappers.item;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import mindustry.type.Item;
import randomMindustry.RMVars;
import randomMindustry.texture.TextureManager;

import static randomMindustry.RMVars.itemStringGen;
import static randomMindustry.mappers.item.ItemMapper.r;

public class CustomItem extends Item {
    public CustomItem(String name) {
        super(name, new Color(r.random(0.3f, 1f), r.random(0.3f, 1f), r.random(0.3f, 1f)));

        explosiveness = r.random(1f);
        radioactivity = r.random(1f);
        flammability = r.random(1f);
        charge = r.random(1f);

        localizedName = itemStringGen.generateName();
        description = itemStringGen.generateDescription(this);

        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(RMVars.tierStat, t -> {
            ItemPack tier = ItemMapper.getPack(this);
            t.add(tier.tier + " " + tier.globalTier + " (" + tier.localTier + ")");
        });
    }

    @Override
    public void loadIcon() {
        TextureRegion region = new TextureRegion(Core.atlas.find("random-mindustry-items"));
        int sprite = r.random(0, RMVars.itemSprites - 1);
        int x = (sprite % RMVars.itemSpriteX) * 32;
        int y = (sprite / RMVars.itemSpriteX) * 32;
        region.set(x + region.getX(), y + region.getY(), 32, 32);
        fullIcon = uiIcon = TextureManager.alloc(region);
        TextureManager.recolorRegion(fullIcon, color);
    }
}
