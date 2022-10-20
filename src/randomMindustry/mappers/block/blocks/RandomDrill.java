package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.meta.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomDrill extends Drill implements RandomBlock {
    public static final Seq<RandomDrill> last = new Seq<>();
    public final int id;
    public Item mainItem;

    public RandomDrill(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    @Override
    public TechTree.TechNode generateNode() {
        techNode = new TechTree.TechNode(
                last.size == 0 ? TechTree.context() : last.random(r).techNode,
                this,
                researchRequirements()
        );
        if (r.chance(0.5) && last.size > 0) last.remove(0);
        last.add(this);
        return techNode;
    }

    @Override
    public int getTier() {
        return id + 1;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }

    public void generate() {
        if (id == 0) {
            alwaysUnlocked = true;
            last.clear();
        }
        this.tier = id + 1;

        stats = new Stats();
        size = 2;
        health = Mathf.round(r.random(70, 100) * this.tier * size, 5);

        drillTime = 600f / this.tier;
        int tier = (this.tier - 1) * 2;
        requirements(Category.production, ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(6, 12) * size, 2), r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        localizedName = mainItem.localizedName + " Drill";
    }

    @Override
    public float getDrillTime(Item item) {
        if (!(item instanceof CustomItem)) return drillTime;
        return 600f * item.hardness / tier;
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = pixmapRegion;
        topRegion = pixmapTopRegion;
        rotatorRegion = pixmapRotatorRegion;
        itemRegion = Core.atlas.find("drill-item-" + size);
        fullIcon = uiIcon = pixmapIcon;
    }

    private TextureRegion pixmapRegion;
    private TextureRegion pixmapTopRegion;
    private TextureRegion pixmapRotatorRegion;
    private TextureRegion pixmapIcon;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, mainItem.color);
        Pixmap region = from.crop(0, 0, 64, 64);
        Pixmap topRegion = from.crop(64, 0, 64, 64);
        Pixmap rotatorRegion = from.crop(128, 0, 64, 64);
        Pixmap icon = region.copy();
        icon.draw(topRegion, true);
        icon.draw(rotatorRegion, true);
        pixmapRegion = TextureManager.alloc(region);
        pixmapTopRegion = TextureManager.alloc(topRegion);
        pixmapRotatorRegion = TextureManager.alloc(rotatorRegion);
        pixmapIcon = TextureManager.alloc(icon);
        pixmapLoaded = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(drillSprites.random(packer, 192, 64, cr));
    }

    public void reloadIcons() {
        createSprites(drillSprites.random(192, 64, cr));
        applyIcons();
    }
}
