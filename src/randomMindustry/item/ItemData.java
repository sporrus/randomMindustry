package randomMindustry.item;

import arc.struct.*;
import mindustry.type.*;

public class ItemData {
    private static final ObjectMap<Item, ItemData> data = new ObjectMap<>();

    public static ItemData get(Item item) {
        return data.get(item);
    }

    public ItemData(Item item) {
        data.put(item, this);
    }

    public float hue = 0;
}
