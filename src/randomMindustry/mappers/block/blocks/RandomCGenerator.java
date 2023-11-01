package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.power.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static mindustry.Vars.*;
import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomCGenerator extends ConsumeGenerator implements RandomBlock{
    public static final Seq<RandomCGenerator> last = new Seq<>();
    public final int id;
    public Item mainItem;
    public int consumeType;
    public boolean handleExplosive;
    
    public RandomCGenerator(String name, int id){
        super(name + id);
        this.id = id;
        generate();
    }
    
    public void generate(){
        if(id == 0) last.clear();
        stats = new Stats();
        techNode = null;
        health = Mathf.round(r.random(50, 100) * size * getTier(), 5);
        
        size = r.random(1, 3);
        requirements(Category.power, ItemMapper.getItemStacks(getTier(), r.random(1, 3), () -> Mathf.round(r.random(5, 25) * size, 5), r));
        mainItem = Seq.with(requirements).sort((a, b) -> ((CustomItem)b.item).globalTier - ((CustomItem)a.item).globalTier).get(0).item;
        researchCostMultiplier = 0.6f;
        
        powerProduction = r.random(1f, 2f) * getTier();
        itemDuration = r.random(60f, 240f);
        consumeEffect = Fx.absorb.wrap(mainItem.color);
        
        consumeType = r.random(2);
        switch(consumeType){
            case 0 -> {
                consume(new ConsumeItemFlammable());
                if(id > 4){
                    handleExplosive = r.nextBoolean();
                    consume(handleExplosive ? new ConsumeItemExplosive() : new ConsumeItemExplode());
                }
            }
            case 1 -> consume(new ConsumeItemRadioactive());
            case 2 -> consume(new ConsumeItemCharged());
        }
        
        if(!headless){
            localizedName = Core.bundle.format("cgen.rm-name", mainItem.localizedName);
            description = Core.bundle.format("cgen.rm-description", powerString(), materialString());
            if(id > 4) description += " " + (handleExplosive ? Core.bundle.get("cantexplode") : Core.bundle.get("canexplode"));
        }
    }
    
    private TextureRegion pixmapRegion;
    private TextureRegion pixmapTopRegion;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from){
        TextureManager.recolorRegion(from, mainItem.color);
        Pixmap region = from.crop(0, 0, size * 32, size * 32);
        Pixmap topRegion = from.crop(0, size * 32, size * 32, size * 32);
        pixmapRegion = TextureManager.alloc(region);
        pixmapTopRegion = TextureManager.alloc(topRegion);
        pixmapLoaded = true;
    }
    
    public void reloadIcons(){
        createSprites(cgenSprites.get(size).random(size * 32, size * 64, cr));
        applyIcons();
    }
    
    @Override
    public void createIcons(MultiPacker packer){
        createSprites(cgenSprites.get(size).random(packer, size * 32, size * 64, cr));
    }
    
    @Override
    public void load(){
        super.load();
        if(pixmapLoaded) applyIcons();
    }
    
    public void applyIcons(){
        region = pixmapRegion;
        /* i did not think this through
        drawer = new DrawMulti(new DrawDefault(), new DrawWarmupRegion()) */
        fullIcon = uiIcon = pixmapRegion;
    }

    @Override
    public void loadIcon(){}
    
    @Override
    public void setStats(){
        super.setStats();
        stats.add(seedStat, seedStatValue);
        stats.add(tierStat, t -> t.add(Integer.toString(getTier())));
    }
    
    @Override
    public TechTree.TechNode generateNode(){
        if(techNode != null) return techNode;
        techNode = new TechTree.TechNode(
                last.size == 0 ? TechTree.context() : last.random(r).techNode,
                this,
                researchRequirements()
        );
        if(r.chance(0.5) && last.size > 0) last.remove(0);
        last.add(this);
        return techNode;
    }
    
    @Override
    public int getTier(){
        return id + 1;
    }
    
    public String powerString(){
        if(powerProduction < 3f) return "small";
        else if(powerProduction < 6f) return "moderate";
        else return "large";
    }
    
    public String materialString(){
        return switch(consumeType){
            case 0 -> "flammable";
            case 1 -> "charged";
            case 2 -> "radioactive";
            default -> "how";
        };
    }
}
