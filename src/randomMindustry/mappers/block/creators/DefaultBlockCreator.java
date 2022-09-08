package randomMindustry.mappers.block.creators;

import mindustry.type.*;
import mindustry.world.*;
import randomMindustry.mappers.item.*;

public class DefaultBlockCreator implements BlockCreator {
    @Override
    public Block create(String name) {
        return new Block(name);
    }

    @Override
    public void edit(Block block) {
        block.requirements = new ItemStack[]{new ItemStack(ItemMapper.generatedItems.first(), 1)};
        block.alwaysUnlocked = true;

        block.localizedName = "unreal block name";
        block.description = "unreal block description";
    }

    @Override
    public boolean has(Block block) {
        return true;
    }
}
