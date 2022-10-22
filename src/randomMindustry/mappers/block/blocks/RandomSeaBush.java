package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.blocks.environment.*;
import randomMindustry.texture.*;

import static randomMindustry.mappers.block.BlockMapper.*;
import static randomMindustry.RMVars.*;

// lets pretend these are plants that are not underwater
public class RandomSeaBush extends SeaBush implements RandomBlock{
    public final int id;
    public Color color;
    
    public RandomSeaBush(String name, int id){
        super(name + id);
        this.id = id;
        generate();
    }

    public void generate(){
        variants = 1;

        lobesMin = r.random(2, 4);
        lobesMax = r.random(8, 10);
        magMin = r.random(2f, 5f);
        magMax = r.random(8f, 15f);
        sclMin = r.random(30f, 45f);
        sclMax = r.random(50f, 100f);
        origin = r.random(0.01f, 0.03f);
        spread = r.random(40f);
        color = new Color(cr.random(0.3f, 1f), cr.random(0.3f, 1f), cr.random(0.3f, 1f));
    }

    private TextureRegion pixmapRegion, pixmapCenter;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from){
        TextureManager.recolorRegion(from, color);
        pixmapRegion = TextureManager.alloc(from.crop(0, 0, 53, 52));
        pixmapCenter = TextureManager.alloc(from.crop(53, 52, 53, 52));
        pixmapLoaded = true;
    }

    @Override
    public void reloadIcons(){
        createSprites(plantSprites.random(106, 52, cr));
        applyIcons();
    }

    @Override
    public void createIcons(MultiPacker packer){
        createSprites(plantSprites.random(packer, 106, 52, cr));
    }

    @Override
    public void load(){
        super.load();

        if(pixmapLoaded) applyIcons();
    }

    public void applyIcons(){
        region = fullIcon = uiIcon = botRegion = pixmapRegion;
        centerRegion = pixmapCenter;
    }

    @Override
    public void loadIcon() {}

    // useless
    @Override
    public int getTier(){
        return 0;
    }

    // skul
    @Override
    public TechTree.TechNode generateNode() {
        return null;
    }
}
