package randomMindustry.mappers.block.blocks;

import mindustry.Vars;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import randomMindustry.mappers.block.BlockMapper;
import randomMindustry.mappers.item.CustomItem;
import randomMindustry.mappers.item.ItemMapper;
import randomMindustry.texture.TextureManager;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomOre extends OreBlock implements RandomBlock {
    public RandomOre(String name) {
        super(name);
    }

    @Override
    public void edit() {
        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof OreBlock && !BlockMapper.generated(b)).random(r);
        this.region = TextureManager.alloc(copyBlock.region);
        this.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        this.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        this.variantRegions = copyBlock.variantRegions;
//        for (int i = 0; i < this.variantRegions.length; i++)
//            this.variantRegions[i] = TextureManager.alloc(this.variantRegions[i]);
//        TextureManager.hueRegion(this.region, hue);
//        TextureManager.hueRegion(this.fullIcon, hue);
//        TextureManager.hueRegion(this.uiIcon, hue);
//        for (TextureRegion region : this.variantRegions)
//            TextureManager.hueRegion(region, hue);
        this.size = this.region.width / 32;

        CustomItem item = ItemMapper.getLockedPacksByTier("drill").random(r).random(true);
        this.setup(item);
        float hue = item.hue;
    }
}
