package randomMindustry.mappers.item;

import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.graphics.*;
import mindustry.type.*;
import randomMindustry.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.item.ItemMapper.*;

public class CustomItem extends Item {
    public static int lastGlobalTier = 0;
    public ItemTierType tierType;
    public int globalTier;
    public int localTier;
    public boolean locked;

    public CustomItem(String name) {
        super(name, new Color(r.random(0.3f, 1f), r.random(0.3f, 1f), r.random(0.3f, 1f)));

        globalTier = (lastGlobalTier++) / 3;
        localTier = globalTier / 2;
        tierType = globalTier % 2 == 0 ? ItemTierType.craft : ItemTierType.drill;
        locked = true;

        hardness = localTier + 1;
        explosiveness = r.random(1f);
        radioactivity = r.random(1f);
        flammability = r.random(1f);
        charge = r.random(1f);
        cost = r.random(0.25f, 1f);

        localizedName = itemStringGen.generateName();
        description = itemStringGen.generateDescription(this);

        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(RMVars.tierStat, t -> t.add(tierType + " " + globalTier + " (" + localTier + ")"));

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
