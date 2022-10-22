package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.struct.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.world.blocks.environment.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomOre extends OreBlock implements RandomBlock {
    public static final Seq<RandomOre> all = new Seq<>();
    public final int id;

    public RandomOre(String name, int id) {
        super(name + id, Items.copper);
        all.add(this);
        this.id = id;
        generate();
    }

    public void generate() {
        setup(ItemMapper.generatedItems
                .selectTierType(CustomItem.TierType.drill)
                .selectLocalTier(getTier())
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
            TextureManager.shadow(pixmap);
            packer.add(MultiPacker.PageType.environment, name + (i + 1), pixmap);
        }
    }

    @Override
    public void reloadIcons() {
        Pixmap ore = oreSprites.random(96, 32, cr);
        for (int i = 0; i < variants; i++) {
            Pixmap pixmap = ore.crop(i * 32, 0, 32, 32);
            TextureManager.recolorRegion(pixmap, itemDrop.color);
            TextureManager.shadow(pixmap);
            TextureRegion region = variantRegions[i];
            region.texture.draw(pixmap, region.getX(), region.getY());
        }
    }

    @Override
    public int getTier() {
        return id / 3;
    }

    @Override
    public TechTree.TechNode generateNode() {
        return null;
    }
}
