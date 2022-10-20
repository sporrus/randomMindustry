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
    public int tier;

    public RandomRouter(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    @Override
    public TechTree.TechNode generateNode() {
        RandomConveyor conv = (RandomConveyor) generatedBlocks.select(b -> b instanceof RandomConveyor).find(b -> ((RandomConveyor)b).id == id);
        techNode = new TechTree.TechNode(
                conv.techNode,
                this,
                researchRequirements() // also also intended
        );
        return techNode;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }

    public void generate() {
        tier = id + 1;

        stats = new Stats();
        size = 1;
        health = Mathf.round(r.random(30, 50) * tier, 1);

        requirements(Category.distribution, ItemMapper.getItemStacks(tier - 1, r.random(1, 3), () -> r.random(1, 3), r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;

        speed = 16f / tier;

        localizedName = mainItem.localizedName + " Router";
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
        topRegion = Core.atlas.find("duct-router-top");
    }

    private TextureRegion pixmapRegion;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, mainItem.color);
        pixmapRegion = TextureManager.alloc(from);
        pixmapLoaded = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(routerSprites.random(packer, 32, cr));
    }

    public void reloadIcons() {
        createSprites(routerSprites.random(32, cr));
        applyIcons();
    }
}
