package com.gorodmi.randomMindustry;

import mindustry.type.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemPack {
    public List<Item> locked = new ArrayList<>();
    public List<Item> all = new ArrayList<>();
    public String tag = "null";
    public int tier = 0;

    public ItemPack(String tag, int tier, Item... items) {
        Collections.addAll(all, items);
        locked = new ArrayList<>(all);
        this.tag = tag;
        this.tier = tier;
    }
}
