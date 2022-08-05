package randomMindustry.block.creators;

import arc.math.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import randomMindustry.texture.*;

public class DefaultBlockCreator implements BlockCreator {
    @Override
    public Block create(String name) {
        return null;
    }

    @Override
    public void edit(Block block) {
        block.localizedName = "unreal block name";
        block.description = "unreal block description";
        block.stats = new Stats();
        float hue = Mathf.random(360f);
        TextureManager.hueRegion(block.region, hue);
        TextureManager.hueRegion(block.fullIcon, hue);
        TextureManager.hueRegion(block.uiIcon, hue);
    }

    @Override
    public boolean has(Block block) {
        return true;
    }
}
