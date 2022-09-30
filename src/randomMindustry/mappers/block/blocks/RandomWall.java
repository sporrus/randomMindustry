package randomMindustry.mappers.block.blocks;

import arc.math.Mathf;
import mindustry.Vars;
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
    }

    @Override
    public void edit() {
        this.health = Mathf.round(r.random(100, 2000), 50);
        this.stats.add(RMVars.seedStat, RMVars.seedStatValue);

        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof Wall && !BlockMapper.generated(b)).random(r);
        this.region = TextureManager.alloc(copyBlock.region);
        this.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        this.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        this.size = this.region.width / 32;
        this.localizedName = "unreal wall name";
        this.description = "unreal wall description";

        this.requirements = ItemMapper.getItemStacks(r.random(0, ItemMapper.maxTier), r.random(1, 5), () -> Mathf.round(r.random(25, 1000), 5));
    }
}
