package randomMindustry.mappers.block.blocks;

import arc.graphics.*;
import arc.struct.*;
import mindustry.graphics.*;
import mindustry.world.blocks.environment.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static mindustry.Vars.*;
import static randomMindustry.mappers.block.BlockMapper.r;
import static randomMindustry.RMVars.*;

public class RandomOre extends OreBlock implements RandomBlock {
    public static final Seq<RandomOre> all = new Seq<>();
    public static int lastTier = 0;

    public RandomOre(String name) {
        super(name, ItemMapper.generatedItems
                .selectTierType(ItemTierType.drill)
                .selectLocalTier((lastTier++) / 3)
                .selectLocked(true)
                .lockNext(false));
        all.add(this);
        variants = 3;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        Pixmap ore = oreSprites.random(packer, 96, 32, r);
        for (int i = 0; i < variants; i++) {
            Pixmap pixmap = ore.crop(i * 32, 0, 32, 32);
            Pixmap shadow = pixmap.crop(0, 0, 32, 32);
            TextureManager.recolorRegion(pixmap, itemDrop.color);
            int offset = pixmap.width / tilesize - 1;
            int shadowColor = Color.rgba8888(0, 0, 0, 0.3f);
            for(int x = 0; x < pixmap.width; x++)
                for(int y = offset; y < pixmap.height; y++)
                    if(shadow.getA(x, y) == 0 && shadow.getA(x, y - offset) != 0)
                        pixmap.set(x, y, shadowColor);
            packer.add(MultiPacker.PageType.environment, name + (i + 1), pixmap);
        }
    }
}
