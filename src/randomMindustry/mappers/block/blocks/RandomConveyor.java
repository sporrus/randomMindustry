package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.*;
import mindustry.world.meta.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomConveyor extends Conveyor implements RandomBlock {
    public static final Seq<RandomConveyor> last = new Seq<>();
    public final int id;
    public Item mainItem;

    public RandomConveyor(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    public void generate() {
        if (id == 0) {
            alwaysUnlocked = true;
            last.clear();
        }
        stats = new Stats();

        size = 1;
        requirements(Category.distribution, ItemMapper.getItemStacks(getTier(), r.random(1, 2), () -> 1, r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;

        health = Mathf.round(r.random(25, 75) * getTier(), 1);
        speed = 0.03f * getTier();
        displayedSpeed = speed * 136;

        localizedName = mainItem.localizedName + " Conveyor";
    }

    private TextureRegion[][] pixmapRegions;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        pixmapRegions = new TextureRegion[5][4];
        for (int i = 0; i < pixmapRegions.length; i++) {
            for (int j = 0; j < pixmapRegions[i].length; j++) {
                Pixmap frame = from.crop(j * 32, i * 32, 32, 32);
                TextureManager.recolorRegion(frame, mainItem.color);
                pixmapRegions[i][j] = TextureManager.alloc(frame);
            }
        }
        pixmapLoaded = true;
    }

    public void reloadIcons() {
        TextureRegion region = Core.atlas.find("random-mindustry-conveyors");
        createSprites(region.texture.getTextureData().getPixmap()
                .crop(region.getX(), region.getY(), region.width, region.height));
        applyIcons();
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(packer.get("random-mindustry-conveyors").crop());
    }

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        regions = pixmapRegions;
        fullIcon = uiIcon = regions[0][0];
    }

    @Override
    public void loadIcon() {}

    @Override
    public int getTier() {
        return id + 1;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(tierStat, t -> t.add(Integer.toString(getTier())));
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
}
