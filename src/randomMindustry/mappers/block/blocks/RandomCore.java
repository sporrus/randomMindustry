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
import randomMindustry.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomCore extends CoreBlock implements RandomBlock{
    public final int id;
    public static RandomCore last = null;
    public static final ObjectMap<Integer, UnitType> types = ObjectMap.of(
        0, UnitTypes.alpha,
        1, UnitTypes.beta,
        2, UnitTypes.gamma,
        3, UnitTypes.mega
    );
    
    public RandomCore(String name, int id){
        super(name + id);
        this.id = id;
        generate();
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

    @Override
    public int getTier(){
        return id * 3 + 1;
    }
    
    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }
    
    public void generate(){
        if (id == 0) {
            isFirstTier = alwaysUnlocked = true;
            last = null;
        }
        size = id + 3;
        itemCapacity = Mathf.round((3000 * getTier()) + r.random(150, 550), 5);
        // not using last because its only used after blocks and other content have loaded
        RandomBlock lastBlock = BlockMapper.generatedBlocks.find(b -> b instanceof RandomCore && ((RandomCore)b).id == this.id - 1);
        requirements(Category.effect, ItemMapper.getItemStacks(getTier() - 1, r.random(3, 5), () -> ((int)Math.max(Mathf.round(r.random(300, 1000) * size, 100), (lastBlock == null ? itemCapacity : ((Block)lastBlock).itemCapacity) - 100)), r));
        unitType = types.get(id, UnitTypes.oct);
        health = Mathf.round(r.random(1000, 3000) * size, 100);
        armor = id * 2;
        unitCapModifier = 15 * getTier();
        researchCostMultiplier = 0.1f;
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
        teamRegion = pixmapTeam;
    }

    private TextureRegion pixmapRegion;
    private TextureRegion pixmapTeam;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        Pixmap region = from.crop(0, 0, 96, 96);
        Pixmap team = from.crop(96, 96, 96, 96);
        TextureManager.recolorRegion(region, new Color(r.random(0.3f, 1f), r.random(0.3f, 1f), r.random(0.3f, 1f)));
        pixmapRegion = TextureManager.alloc(region);
        pixmapTeam = TextureManager.alloc(team);
        pixmapLoaded = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(coreSprites.get(3).random(packer, 192, 96, r));
    }

    public void reloadIcons() {
        createSprites(coreSprites.get(3).random(192, 96, r));
        applyIcons();
    }
}
