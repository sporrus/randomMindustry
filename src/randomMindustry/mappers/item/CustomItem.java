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
    public final int id;
    public ItemTierType tierType;
    public int globalTier;
    public int localTier;
    public boolean locked;

    public CustomItem(String name, int id) {
        super(name + id, Color.red);
        this.id = id;
        generate();
    }

    public void reload() {
        generate();
        reloadIcons();
    }

    public void generate() {
        color = new Color(r.random(0.3f, 1f), r.random(0.3f, 1f), r.random(0.3f, 1f));

        globalTier = id / 3;
        localTier = globalTier / 2;
        tierType = globalTier % 2 == 0 ? ItemTierType.drill : ItemTierType.craft;
        locked = true;

        hardness = localTier + 1;
        explosiveness = r.random(100) / 100f;
        radioactivity = r.random(100) / 100f;
        flammability = r.random(100) / 100f;
        charge = r.random(100) / 100f;
        cost = r.random(0.25f, 0.5f);

        localizedName = itemStringGen.generateName();
        description = itemStringGen.generateDescription(this);
        details = "An otherworldly material... Who knows what this could make?";

        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(RMVars.tierStat, t -> t.add(tierType + " " + globalTier + " (" + localTier + ")"));
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    private boolean pixmapLoaded = false;
    private TextureRegion pixmapIcon;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, color);
        pixmapIcon = TextureManager.alloc(from);
        pixmapLoaded = true;
    }

    public void applyIcons() {
        fullIcon = uiIcon = pixmapIcon;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(itemSprites.random(packer, 32, r));
    }

    public void reloadIcons() {
        createSprites(itemSprites.random(32, r));
        applyIcons();
    }
}
