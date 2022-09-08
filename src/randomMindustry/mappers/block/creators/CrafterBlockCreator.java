package randomMindustry.mappers.block.creators;

import arc.math.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

public class CrafterBlockCreator extends DefaultBlockCreator {
    private final SyncedRand r;

    public CrafterBlockCreator() {
        r = new SyncedRand();
    }

    @Override
    public boolean has(Block block) {
        return block instanceof GenericCrafter;
    }

    @Override
    public Block create(String name) {
        return new GenericCrafter(name) {{
            requirements(Category.crafting, new ItemStack[]{new ItemStack(Items.copper, 1)});
            size = 1;
        }};
    }

    @Override
    public void edit(Block block) {
        CustomItem item;
        ItemPack pack = ItemMapper.getLockedPacksByTier("craft").random(r);
        if (pack == null) item = ItemMapper.getPacksByTier("craft").random(r).random(false);
        else item = pack.random(true);
        ((GenericCrafter) block).outputItems = new ItemStack[]{new ItemStack(item, r.random(1, 10))};
        int tier = ItemMapper.getTier(item);
        block.consumeItems(ItemMapper.getItemStacks(tier - 1, r.random(1, 3), () -> r.random(1, 10)));
        block.stats.add(RMVars.seed, RMVars.seedValue);

        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof GenericCrafter && !BlockMapper.generated(b)).random(r);
        block.region = TextureManager.alloc(copyBlock.region);
        block.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        block.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        block.size = block.region.width / 32;
        super.edit(block);
        block.localizedName = "unreal crafter name";
        block.description = "unreal crafter description";

        block.requirements = ItemMapper.getItemStacks(tier - 1, r.random(1, 5), () -> Mathf.round(r.random(25, 1000), 5));
    }
}
