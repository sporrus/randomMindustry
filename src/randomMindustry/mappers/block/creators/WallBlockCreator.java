package randomMindustry.mappers.block.creators;

import arc.math.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import randomMindustry.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

public class WallBlockCreator extends DefaultBlockCreator {
    private final SyncedRand r;

    public WallBlockCreator() {
        r = new SyncedRand();
    }

    @Override
    public boolean has(Block block) {
        return block instanceof Wall;
    }

    @Override
    public Block create(String name) {
        return new Wall(name) {{
            requirements(Category.defense, new ItemStack[]{new ItemStack(Items.copper, 1)});
            size = 1;
        }};
    }

    @Override
    public void edit(Block block) {
        block.health = Mathf.round(r.random(100, 2000), 50);
        block.stats.add(RMVars.seed, RMVars.seedValue);

        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof Wall && !BlockMapper.generated(b)).random(r);
        block.region = TextureManager.alloc(copyBlock.region);
        block.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        block.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        block.size = block.region.width / 32;
        super.edit(block);
        block.localizedName = "unreal wall name";
        block.description = "unreal wall description";

        block.requirements = ItemMapper.getItemStacks(r.random(0, ItemMapper.maxTier), r.random(1, 5), () -> Mathf.round(r.random(25, 1000), 5));
    }
}
