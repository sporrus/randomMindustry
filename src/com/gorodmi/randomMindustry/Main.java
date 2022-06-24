package com.gorodmi.randomMindustry;

import arc.Events;
import com.gorodmi.randomMindustry.ResourceMapper;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import mindustry.type.ItemStack;
import mindustry.world.blocks.production.GenericCrafter;

public class Main extends Mod{
    public Main(){
        Events.on(EventType.ClientLoadEvent.class, (e) -> {
            ResourceMapper.init();
            BlockMapper.init();
        });
    }
}
