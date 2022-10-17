package randomMindustry.mappers.block.blocks;

import mindustry.content.*;

public interface RandomBlock {
    void generate();
    void reloadIcons();
    int getTier();
    TechTree.TechNode generateNode();
}
