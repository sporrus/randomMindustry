package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomCrafter extends GenericCrafter implements RandomBlock {
    public final int id;
    public CustomItem item;
    public RandomCrafterType type;

    public RandomCrafter(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
        squareSprite = false;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }

    @Override
    public void reload() {
        generate();
        reloadIcons();
    }

    public void generate() {
        int tier = id / 3;

        size = r.random(2, 3);
        health = Mathf.round(r.random(5, 50) * size, 5);

        CustomItemSeq items = ItemMapper.generatedItems
                .selectTierType(ItemTierType.craft)
                .selectLocalTier(tier)
                .selectLocked(true);
        item = items.lockNext(false);
        outputItems = new ItemStack[]{new ItemStack(item, r.random(1, 10))};

        requirements(Category.crafting, ItemMapper.getItemStacks(tier * 2, r.random(1, 5), () -> Mathf.round(r.random(10, 100) * size, 5)));
        ItemStack[] itemStacks = ItemMapper.getItemStacks(tier * 2, r.random(1, 3), () -> r.random(1, 10));
        consumeItems(itemStacks);

        type = RandomCrafterType.random(r);
        localizedName = Core.bundle.format("crafter.rm-name." + type, item.localizedName);
        String output = itemStacks[0].item.localizedName;
        for (int i = 1; i < itemStacks.length; i++) {
            if (i == itemStacks.length - 1) output += " and ";
            else output += ", ";
            output += itemStacks[i].item.localizedName;
        }
        description = Core.bundle.format("crafter.rm-description." + type, output, item.localizedName);
    }

    @Override
    public void loadIcon() {}

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
    }

    private TextureRegion pixmapRegion;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, item.color);
        pixmapRegion = TextureManager.alloc(from);
        pixmapLoaded = true;
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(crafterSprites.get(size).random(packer, size * 32, r));
    }

    public void reloadIcons() {
        createSprites(crafterSprites.get(size).random(size * 32, r));
        applyIcons();
    }

    public enum RandomCrafterType {
        press, cultivator, mixer, smelter, compressor,
        weaver, kiln, pulverizer, centrifuge, condenser,
        melter, creator;

        public static RandomCrafterType random(Rand r) {
            return Seq.with(values()).random(r);
        }
    }
}
