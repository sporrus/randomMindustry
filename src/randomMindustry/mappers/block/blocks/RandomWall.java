package randomMindustry.mappers.block.blocks;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.defense.Wall;
import randomMindustry.RMVars;
import randomMindustry.mappers.block.BlockMapper;
import randomMindustry.mappers.item.ItemMapper;
import randomMindustry.texture.TextureManager;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomWall extends Wall implements RandomBlock {
    public RandomWall(String name) {
        super(name);
        health = Mathf.round(r.random(100, 2000), 50);
        requirements(Category.crafting, ItemMapper.getItemStacks(r.random(0, ItemMapper.maxTier), r.random(1, 5), () -> Mathf.round(r.random(25, 1000), 5)));
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }

    public void edit() {
        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof Wall && !BlockMapper.generated(b)).random(r);
        this.region = TextureManager.alloc(copyBlock.region);
        this.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        this.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        this.size = this.region.width / 32;
        this.localizedName = "unreal wall name";
        this.description = "unreal wall description";
    }
}
