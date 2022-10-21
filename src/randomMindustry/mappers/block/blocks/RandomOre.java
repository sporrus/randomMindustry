package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.blocks.environment.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static mindustry.Vars.*;
import static randomMindustry.mappers.block.BlockMapper.*;
import static randomMindustry.RMVars.*;

public class RandomOre extends OreBlock implements RandomBlock {
    public static final Seq<RandomOre> all = new Seq<>();
    public final int id;

    public RandomOre(String name, int id) {
        super(name + id, Items.copper);
        all.add(this);
        this.id = id;
        generate();
    }

    @Override
    public TechTree.TechNode generateNode() {
        return null;
    }

    @Override
    public int getTier() {
        return id / 3;
    }

    public void generate() {
        setup(ItemMapper.generatedItems
                .selectTierType(ItemTierType.drill)
                .selectLocalTier(id / 3)
                .selectLocked(true)
                .lockNext(false));
        variants = 3;
    }

    // TODO: make this reload
    @Override
    public void createIcons(MultiPacker packer) {
        Pixmap ore = oreSprites.random(packer, 96, 32, cr);
        for (int i = 0; i < variants; i++) {
            Pixmap pixmap = ore.crop(i * 32, 0, 32, 32);
            TextureManager.recolorRegion(pixmap, itemDrop.color);
            shadow(pixmap);
            packer.add(MultiPacker.PageType.environment, name + (i + 1), pixmap);
        }
    }

    @Override
    public void reloadIcons() {
        Pixmap ore = oreSprites.random(96, 32, cr);
        for (int i = 0; i < variants; i++) {
            Pixmap pixmap = ore.crop(i * 32, 0, 32, 32);
            TextureManager.recolorRegion(pixmap, itemDrop.color);
            shadow(pixmap);
            TextureRegion region = variantRegions[i];
            region.texture.draw(pixmap, region.getX(), region.getY());
        }
    }

    public void shadow(Pixmap pixmap) {
        Pixmap shadow = pixmap.crop(0, 0, pixmap.width, pixmap.height);
        int offset = pixmap.width / tilesize - 1;
        int shadowColor = Color.rgba8888(0, 0, 0, 0.3f);
        for(int x = 0; x < pixmap.width; x++)
            for(int y = offset; y < pixmap.height; y++)
                if(shadow.getA(x, y) == 0 && shadow.getA(x, y - offset) != 0)
                    pixmap.set(x, y, shadowColor);
    }
}
