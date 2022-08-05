package randomMindustry.block.creators;

import mindustry.world.*;

public interface BlockCreator {
    Block create(String name);
    void edit(Block block);
    boolean has(Block block);
}
