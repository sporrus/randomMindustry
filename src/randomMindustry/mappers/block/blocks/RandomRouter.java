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

public class RandomRouter extends DuctRouter implements RandomBlock {
    public final int id;
    public Item mainItem;

    public RandomRouter(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    public void generate() {
        stats = new Stats();
        techNode = null;

        size = 1;
        requirements(Category.distribution, ItemMapper.getItemStacks(getTier(), r.random(1, 3), () -> r.random(1, 3), r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;

        health = Mathf.round(r.random(30, 50) * getTier(), 1);
        speed = 16f / getTier();

        localizedName = mainItem.localizedName.split(" ")[0] + " Router";
    }

    private TextureRegion pixmapRegion;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, mainItem.color);
        pixmapRegion = TextureManager.alloc(from);
        pixmapLoaded = true;
    }

    @Override
    public void reloadIcons() {
        createSprites(routerSprites.random(32, cr));
        applyIcons();
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(routerSprites.random(packer, 32, cr));
    }

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
        topRegion = Core.atlas.find("duct-router-top");
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
        if (techNode != null) return techNode;
        RandomConveyor conv = (RandomConveyor) generatedBlocks.select(b -> b instanceof RandomConveyor).find(b -> ((RandomConveyor)b).id == id);
        techNode = new TechTree.TechNode(
                conv.techNode,
                this,
                researchRequirements() // also also intended
        );
        return techNode;
    }
}
