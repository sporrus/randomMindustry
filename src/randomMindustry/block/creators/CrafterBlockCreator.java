package randomMindustry.block.creators;

import arc.math.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.defense.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;
import randomMindustry.block.*;
import randomMindustry.item.*;
import randomMindustry.texture.*;

public class CrafterBlockCreator extends DefaultBlockCreator {
    private final Rand rand;

    public CrafterBlockCreator() {
        rand = new Rand(SeedManager.getSeed());
    }

    @Override
    public boolean has(Block block) {
        return block instanceof GenericCrafter;
    }

    @Override
    public Block create(String name) {
        return new GenericCrafter(name) {{
            requirements(Category.crafting, new ItemStack[]{new ItemStack(Items.copper, 1)});
            size = rand.random(1, 2);
        }};
    }

    @Override
    public void edit(Block block) {
        Item item;
        ItemPack pack = ItemMapper.getLockedPacksByTier("craft").random(rand);
        if (pack == null) item = ItemMapper.getPacksByTier("craft").random(rand).random(false);
        else item = pack.random(true);
        ((GenericCrafter) block).outputItems = new ItemStack[]{new ItemStack(item, rand.random(1, 10))};
        int tier = ItemMapper.getTier(item);
        int itemCount = 1;
        while (rand.chance(0.5) && itemCount < 5) itemCount++;
        Seq<ItemStack> stacks = new Seq<>();
        ItemPack packs = ItemMapper.combine(ItemMapper.getPacksInGlobalTierRange(0, tier - 1));
        packs.lock();
        for (int i = 0; i < itemCount; i++) {
            if (i == 0) {
                Item it = ItemMapper.getPackByGlobalTier(tier - 1).random(false);
                stacks.add(new ItemStack(it, rand.random(1, 10)));
                packs.lock(it);
            } else {
                stacks.add(new ItemStack(packs.random(true), rand.random(1, 10)));
            }
        }
        RandomUtil.shuffle(stacks, rand);
        block.consumeItems(stacks.toArray(ItemStack.class));

        Block copyBlock = Vars.content.blocks().select((b) -> b instanceof GenericCrafter && !BlockMapper.generated(b)).random(rand);
        block.region = TextureManager.alloc(copyBlock.region);
        block.fullIcon = TextureManager.alloc(copyBlock.fullIcon);
        block.uiIcon = TextureManager.alloc(copyBlock.uiIcon);
        block.size = block.region.width / 32;
        super.edit(block);
        block.localizedName = "unreal crafter name";
        block.description = "unreal crafter description";
    }
}
