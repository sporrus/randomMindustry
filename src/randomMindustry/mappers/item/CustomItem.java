package randomMindustry.mappers.item;

import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.PixmapRegion;
import mindustry.graphics.MultiPacker;
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
        cost = r.random(0.25f, 1f);

        localizedName = itemStringGen.generateName();
        description = itemStringGen.generateDescription(this);

        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(RMVars.tierStat, t -> {
            ItemPack tier = ItemMapper.getPack(this);
            t.add(tier.tier + " " + tier.globalTier + " (" + tier.localTier + ")");
        });

        generateIcons = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        PixmapRegion region = packer.get("random-mindustry-items");
        int sprite = r.random(0, RMVars.itemSprites - 1);
        int x = (sprite % RMVars.itemSpriteX) * 32;
        int y = (sprite / RMVars.itemSpriteX) * 32;
        Pixmap pixmap = region.crop(x, y, 32, 32);
        TextureManager.recolorRegion(pixmap, color);
        fullIcon = uiIcon = TextureManager.alloc(pixmap);
    }

    @Override
    public void loadIcon() {}
}
