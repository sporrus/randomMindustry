package randomMindustry.mappers.block.creators;

import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import randomMindustry.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

public class OreBlockCreator extends DefaultBlockCreator {
    private final SyncedRand r;

    public OreBlockCreator() {
        r = new SyncedRand();
    }

    @Override
    public boolean has(Block block) {
        return block instanceof OreBlock;
    }

    @Override
    public Block create(String name) {
        return new OreBlock(name, Items.copper) {{
            size = 1;
        }};
    }

    @Override
    public void edit(Block block) {
         Vars.renderer.blocks.floor.endc();

        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof OreBlock && !BlockMapper.generated(b)).random(r);
        block.region = TextureManager.alloc(copyBlock.region);
        block.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        block.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        block.variantRegions = copyBlock.variantRegions;
//        for (int i = 0; i < block.variantRegions.length; i++)
//            block.variantRegions[i] = TextureManager.alloc(block.variantRegions[i]);
        block.size = block.region.width / 32;
        super.edit(block);
        CustomItem item = ItemMapper.getLockedPacksByTier("drill").random(r).random(true);
        ((OreBlock) block).setup(item);
        float hue = item.hue;
        TextureManager.hueRegion(block.region, hue);
        TextureManager.hueRegion(block.fullIcon, hue);
        TextureManager.hueRegion(block.uiIcon, hue);
//        for (TextureRegion region : block.variantRegions)
//            TextureManager.hueRegion(region, hue);
    }
}
