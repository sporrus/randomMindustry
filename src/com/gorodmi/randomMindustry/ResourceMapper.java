package com.gorodmi.randomMindustry;

import mindustry.content.Items;
import mindustry.type.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceMapper {
    public static Map<String, ItemPack> itemMap = new HashMap<>();

    // TODO: make randomized items
    public static void init() {
        itemMap.put("drill0", new ItemPack("drill", 0,
                Items.copper, Items.lead, Items.scrap
        ));
    }

    public static Item getLocked(String tag, int tier) {
        ItemPack pack = itemMap.get(tag + tier);
        if (pack.locked.size() <= 0) return null;
        Item item = randomList(pack.locked);
        pack.locked.remove(item);
        return item;
    }

    public static Item getAll(String tag, int tier) {
        ItemPack pack = itemMap.get(tag + tier);
        if (pack.all.size() <= 0) return null;
        return randomList(pack.all);
    }

    public static <T> T randomList(List<T> list) {
        return list.get(getRandomInt(list.size()));
    }

    public static int getRandomInt(int max) {
        return (int) (Math.random() * max);
    }
}
