package randomMindustry.block.creators;

import mindustry.world.*;

public interface BlockCreator {
    /**
     * Returns a created block with specified name
     * @param name block name
     * @return created block
     */
    Block create(String name);
    /**
     * Edits a block
     * @param block block to be edited
     */
    void edit(Block block);
    /**
     * Returns true if block was created with this creator
     * @param block block
     * @return block was created with this creator
     */
    boolean has(Block block);
}
