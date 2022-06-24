package com.gorodmi.randomMindustry;

import mindustry.content.Items;
import mindustry.type.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceMapper {
    public static String[] tagPriority = new String[]{"hand", "drill", "craft"};
    public static Map<String, ItemPack> itemMap = new HashMap<>();

    // TODO: make randomized items
    public static void init() {
        itemMap.put("hand0", new ItemPack("hand", 0,
                Items.copper, Items.lead, Items.scrap
        ));
        itemMap.put("drill0", new ItemPack("drill", 0,
                Items.sand, Items.coal
        ));
        itemMap.put("drill1", new ItemPack("drill", 1,
                Items.titanium
        ));
        itemMap.put("drill2", new ItemPack("drill", 2,
                Items.thorium
        ));
        itemMap.put("craft0", new ItemPack("craft", 0,
                Items.metaglass, Items.graphite, Items.silicon, Items.sporePod, Items.pyratite
        ));
        itemMap.put("craft1", new ItemPack("craft", 1,
                Items.plastanium, Items.phaseFabric, Items.surgeAlloy, Items.sporePod, Items.blastCompound
        ));
    }

    public static Item getLocked(String tag, int tier) {
        ItemPack pack = itemMap.get(tag + tier);
        return getLocked(pack);
    }

    public static Item getLocked(ItemPack pack) {
        if (pack.locked.size() <= 0) return null;
        Item item = randomList(pack.locked);
        if (item == null) return null;
        pack.locked.remove(item);
        return item;
    }

    public static Item getLocked(String tag) {
        int tier = 0;
        Item item = getLocked(tag, tier);
        while (item == null) {
            tier++;
            if (!itemMap.containsKey(tag + tier)) return null;
            item = getLocked(tag, tier);
        }
        return item;
    }

    public static Item getLocked() {
        ItemPack pack = randomList(new ArrayList<>(itemMap.values()));
        return getLocked(pack);
    }

    public static Item getAll(String tag, int tier) {
        ItemPack pack = itemMap.get(tag + tier);
        return getAll(pack);
    }

    public static Item getAll(ItemPack pack) {
        if (pack.all.size() <= 0) return null;
        Item item = randomList(pack.all);
        pack.all.remove(item);
        return item;
    }

    public static Item getAll() {
        ItemPack pack = randomList(new ArrayList<>(itemMap.values()));
        return getAll(pack);
    }

    public static <T> T randomList(List<T> list) {
        return list.get(getRandomInt(list.size()));
    }

    public static int getRandomInt(int max) {
        return (int) (Math.random() * max);
    }
}
