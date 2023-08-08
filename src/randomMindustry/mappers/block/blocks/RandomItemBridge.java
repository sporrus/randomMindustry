package randomMindustry.mappers.block.blocks;

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
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomItemBridge extends BufferedItemBridge implements RandomBlock {
    public final int id;
    public Item mainItem;

    public RandomItemBridge(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    public void generate() {
        stats = new Stats();
        techNode = null;

        size = 1;
        requirements(Category.distribution, ItemMapper.getItemStacks(getTier(), r.random(1, 2), () -> r.random(1, 10), r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;

        health = Mathf.round(r.random(30, 60) * getTier(), 1);
        itemCapacity = 10 * getTier();
        range = getTier() + 2;
        speed = 16f / getTier();

        localizedName = mainItem.localizedName.split(" ")[0] + " Bridge";
    }

    private boolean pixmapLoaded = false;
    private TextureRegion pixmapRegion;
    private TextureRegion pixmapEnd;
    private TextureRegion pixmapBridge;
    private TextureRegion pixmapArrow;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, mainItem.color);
        pixmapRegion = TextureManager.alloc(from.crop(0, 0, 32, 32));
        pixmapEnd = TextureManager.alloc(from.crop(32, 0, 32, 32));
        pixmapBridge = TextureManager.alloc(from.crop(64, 0, 32, 32));
        pixmapArrow = TextureManager.alloc(from.crop(96, 0, 32, 32));
        pixmapLoaded = true;
    }

    public void reloadIcons() {
        createSprites(bridgeSprites.random(128, 32, cr));
        applyIcons();
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(bridgeSprites.random(packer, 128, 32, cr));
    }

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
        endRegion = pixmapEnd;
        bridgeRegion = pixmapBridge;
        arrowRegion = pixmapArrow;
    }

    @Override
    public void loadIcon() {}

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(tierStat, t -> t.add(Integer.toString(getTier())));
    }

    @Override
    public int getTier() {
        return id + 1;
    }

    @Override
    public TechTree.TechNode generateNode() {
        if (techNode != null) return techNode;
        RandomRouter router = (RandomRouter) generatedBlocks.select(b -> b instanceof RandomRouter).find(b -> ((RandomRouter)b).id == id);
        techNode = new TechTree.TechNode(
                router.techNode,
                this,
                researchRequirements() // also also intended
        );
        return techNode;
    }
}
