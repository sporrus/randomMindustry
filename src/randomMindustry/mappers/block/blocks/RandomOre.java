package randomMindustry.mappers.block.blocks;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.graphics.MultiPacker;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import randomMindustry.mappers.block.BlockMapper;
import randomMindustry.mappers.item.CustomItem;
import randomMindustry.mappers.item.ItemMapper;
import randomMindustry.texture.TextureManager;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomOre extends OreBlock implements RandomBlock {
    public static final Seq<RandomOre> all = new Seq<>();

    public RandomOre(String name) {
        super(name, ItemMapper.getLockedPacksByTier("drill").random(r).random(true));
        all.add(this);
    }

    @Override
    public void edit() {
        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof OreBlock && !BlockMapper.generated(b)).random(r);
        float hue = ((CustomItem) itemDrop).hue;
        this.region = TextureManager.alloc(copyBlock.region);
        this.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        this.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        this.variantRegions = copyBlock.variantRegions;
        this.size = this.region.width / 32;
//        for (int i = 0; i < this.variantRegions.length; i++)
//            this.variantRegions[i] = TextureManager.alloc(this.variantRegions[i]);
//        for (TextureRegion region : this.variantRegions)
//            TextureManager.hueRegion(region, hue);
        TextureManager.hueRegion(this.region, hue);
        TextureManager.hueRegion(this.fullIcon, hue);
        TextureManager.hueRegion(this.uiIcon, hue);
    }
}
