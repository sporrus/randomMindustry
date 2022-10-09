package randomMindustry.mappers.block.blocks;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Items;
import mindustry.graphics.MultiPacker;
import mindustry.world.Block;
import mindustry.world.blocks.environment.OreBlock;
import randomMindustry.mappers.block.BlockMapper;
import randomMindustry.mappers.item.CustomItem;
import randomMindustry.mappers.item.ItemMapper;
import randomMindustry.texture.TextureManager;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomOre extends OreBlock implements RandomBlock {
    public static final Seq<RandomOre> all = new Seq<>();

    public RandomOre(String name) {
        super(name, ItemMapper.getLockedPacksByTier("drill").random(r).random(true));
        all.add(this);
    }
}
