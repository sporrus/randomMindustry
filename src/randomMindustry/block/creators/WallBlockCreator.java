package randomMindustry.block.creators;

import arc.math.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import randomMindustry.*;
import randomMindustry.block.*;
import randomMindustry.texture.*;

public class WallBlockCreator extends DefaultBlockCreator {
    private final Rand rand;

    public WallBlockCreator() {
        rand = new Rand(SeedManager.getSeed());
    }

    @Override
    public boolean has(Block block) {
        return block instanceof Wall;
    }

    @Override
    public Block create(String name) {
        return new Wall(name) {{
            requirements(Category.defense, new ItemStack[]{new ItemStack(Items.copper, 1)});
            size = rand.random(1, 2);
        }};
    }

    @Override
    public void edit(Block block) {
        block.health = Mathf.round(rand.random(100, 2000), 50);

        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof Wall && !BlockMapper.generated(b)).random(rand);
        block.region = TextureManager.alloc(copyBlock.region);
        block.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        block.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        block.size = block.region.width / 32;
        super.edit(block);
        block.localizedName = "unreal wall name";
        block.description = "unreal wall description";
    }
}
