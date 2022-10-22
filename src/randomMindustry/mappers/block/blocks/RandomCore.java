package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.storage.*;
import mindustry.world.meta.*;
import randomMindustry.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomCore extends CoreBlock implements RandomBlock{
    public final int id;
    public static RandomCore last = null;
    public static final ObjectMap<Integer, UnitType> types = ObjectMap.of(
        0, UnitTypes.alpha,
        1, UnitTypes.beta,
        2, UnitTypes.gamma,
        3, UnitTypes.mega
    );
    public Item mainItem;
    
    public RandomCore(String name, int id){
        super(name + id);
        this.id = id;
        generate();
    }

    public void generate(){
        if (id == 0) {
            isFirstTier = alwaysUnlocked = true;
            last = null;
        }
        stats = new Stats();

        size = Math.min(id + 3, 16);
        itemCapacity = Mathf.round(r.random(1000, 3000) * size, 100);
        RandomCore lastBlock = (RandomCore) BlockMapper.generatedBlocks.find(b -> b instanceof RandomCore && ((RandomCore)b).id == this.id - 1);
        requirements(Category.effect, ItemMapper.getItemStacks(getTier(), r.random(3, 5), () -> Mathf.round(Math.min(r.random(300, 1000) * size, (lastBlock == null ? this : lastBlock).itemCapacity), 100), r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem) b.item).globalTier - ((CustomItem) a.item).globalTier).get(0).item;
        researchCostMultiplier = 0.1f;

        unitType = types.get(id, UnitTypes.oct);
        health = Mathf.round(r.random(300, 1000) * size * getTier(), 100);
        armor = id * 2;
        unitCapModifier = 15 * getTier();
    }

    private TextureRegion pixmapRegion;
    private TextureRegion pixmapTeam;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        Pixmap region = from.crop(0, 0, 96, 96);
        Pixmap team = from.crop(96, 96, 96, 96);
        TextureManager.recolorRegion(region, mainItem.color);
        pixmapRegion = TextureManager.alloc(region);
        pixmapTeam = TextureManager.alloc(team);
        pixmapLoaded = true;
    }

    public void reloadIcons() {
        createSprites(coreSprites.get(3).random(192, 96, cr));
        applyIcons();
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(coreSprites.get(3).random(packer, 192, 96, cr));
    }

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
        teamRegion = pixmapTeam;
    }

    @Override
    public void loadIcon() {}

    @Override
    public int getTier(){
        return id * 3 + 1;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(tierStat, t -> t.add(Integer.toString(getTier())));
    }

    @Override
    public TechTree.TechNode generateNode() {
        if (id == 0) return null; // no need to generate, root
        techNode = new TechTree.TechNode(
                last == null ? TechTree.context() : last.techNode,
                this,
                researchRequirements()
        );
        last = this;
        return techNode;
    }
}
