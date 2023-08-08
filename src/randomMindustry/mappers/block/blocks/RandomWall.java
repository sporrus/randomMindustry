package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.meta.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomWall extends Wall implements RandomBlock {
    public static final Seq<RandomWall> last = new Seq<>();
    public final int id;
    public Item mainItem;

    public RandomWall(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    public void generate() {
        if (id == 0) {
            last.clear();
        }
        stats = new Stats();
        techNode = null;

        size = r.random(1, 2);
        requirements(Category.defense, ItemMapper.getItemStacks(getTier(), r.random(1, 3), () -> 6 * size * size, r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        researchCostMultiplier = 0.2f;

        health = Mathf.round(r.random(150, 450) * size * getTier(), 5);

        localizedName = mainItem.localizedName.split(" ")[0] + " Wall";
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
        createSprites(wallSprites.get(size).random(size * 32, cr));
        applyIcons();
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(wallSprites.get(size).random(packer, size * 32, cr));
    }

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
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
        RandomItemTurret itur = (RandomItemTurret) generatedBlocks.select(b -> b instanceof RandomItemTurret).find(b -> ((RandomItemTurret)b).id == 0);
        techNode = new TechTree.TechNode(
                last.size == 0 ? itur.generateNode() : last.random(r).techNode,
                this,
                researchRequirements()
        );
        if (r.chance(0.5) && last.size > 0) last.remove(0);
        last.add(this);
        return techNode;
    }
}
