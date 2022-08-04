package randomMindustry.block;

import arc.math.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;
import randomMindustry.texture.*;

public class CustomBlock extends Block {
    public CustomBlock(String name) {
        super(name);
        category = Category.crafting;
        buildVisibility = BuildVisibility.shown;
        requirements = new ItemStack[]{new ItemStack(Items.copper, 1)};
        health = 100;
    }

    public void edit() {
        Block block = Vars.content.blocks().select(b -> !(b instanceof CustomBlock) && b.buildVisibility == BuildVisibility.shown).random();
        stats = new Stats();
        float hue = Mathf.random(360f);
        region = TextureManager.alloc(block.region);
        TextureManager.hueRegion(region, hue);
        fullIcon = TextureManager.alloc(block.fullIcon);
        TextureManager.hueRegion(fullIcon, hue);
        uiIcon = TextureManager.alloc(block.uiIcon);
        TextureManager.hueRegion(uiIcon, hue);
        size = region.width / 32;
        init();
    }
}
