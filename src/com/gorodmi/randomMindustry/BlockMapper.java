package com.gorodmi.randomMindustry;

import arc.util.Log;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.world.blocks.production.GenericCrafter;

public class BlockMapper {
    public static void init() {
        Vars.content.blocks().each((block -> {
            if (block instanceof GenericCrafter) {
                Item item = ResourceMapper.getLocked("drill", 0);
                if (item == null) item = ResourceMapper.getAll("drill", 0);
                int count = ResourceMapper.getRandomInt(10)+1;
                ((GenericCrafter) block).outputItems = new ItemStack[]{new ItemStack(
                        item, count
                )};
            }
        }));
    }
}
