package randomMindustry.random;

import arc.struct.*;
import mindustry.*;
import mindustry.game.*;
import randomMindustry.mappers.block.*;
import randomMindustry.mappers.block.blocks.*;

public class RandomLoadouts {
    public static final Seq<Schematic> loadouts = new Seq<>();

    public static void load() {
        BlockMapper.generatedBlocks.select(b -> b instanceof RandomCore).each(b -> {
            RandomCore c = (RandomCore) b;
            Seq<Schematic.Stile> tiles = Seq.with(new Schematic.Stile(c, 0, 0, null, (byte) 0));
            Schematic schematic = new Schematic(tiles, new StringMap(), c.size, c.size);
            loadouts.add(schematic);
            Vars.schematics.getLoadouts().put(c, Seq.with(schematic));
        });
    }
}
