package randomMindustry.mappers.block.blocks;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.production.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;
import randomMindustry.texture.*;

import static randomMindustry.RMVars.*;
import static randomMindustry.mappers.block.BlockMapper.*;

public class RandomCrafter extends GenericCrafter implements RandomBlock {
    public static final Seq<RandomCrafter> last = new Seq<>();
    public final int id;
    public CustomItem item;
    public RandomCrafterType type;

    public RandomCrafter(String name, int id) {
        super(name + id);
        this.id = id;
        generate();
    }

    public void generate() {
        if (id == 0) {
            last.clear();
        }
        stats = new Stats();
        // cancer 2
        Consume[] consumes = consumers.clone();
        consumers = new Consume[0];
        for (Consume consume : consumes) removeConsumer(consume);

        size = r.random(2, 4);
        item = ItemMapper.generatedItems
                .selectTierType(CustomItem.TierType.craft).selectGlobalTier(getTier() + 1)
                .selectLocked(true).lockNext(false);
        requirements(Category.crafting, ItemMapper.getItemStacks(getTier(),
                r.random(1, 5), () -> Mathf.round(r.random(10, 50) * size, 5), r));
        ItemStack[] itemStacks = ItemMapper.getItemStacks(getTier(),
                r.random(1, 3), () -> r.random(1, 5 * size), r);
        consumeItems(itemStacks);
        outputItems = new ItemStack[]{new ItemStack(item, r.random(1, 5 * size))};
        researchCostMultiplier = 0.2f;

        health = Mathf.round(r.random(20, 100) * size * getTier(), 5);
        craftTime = r.random(30f, 120f);
        int maxItems = Math.max(Seq.with(itemStacks).sort((a, b) -> b.amount - a.amount).get(0).amount, outputItems[0].amount);
        itemCapacity = maxItems * 2;

        type = RandomCrafterType.random(r);
        if (!Vars.headless) {
            localizedName = Core.bundle.format("crafter.rm-name." + type, item.localizedName);
            String output = itemStacks[0].item.localizedName;
            for (int i = 1; i < itemStacks.length; i++) {
                if (i == itemStacks.length - 1) output += " and ";
                else output += ", ";
                output += itemStacks[i].item.localizedName;
            }
            description = Core.bundle.format("crafter.rm-description." + type, output, item.localizedName);
        }
    }

    private TextureRegion pixmapRegion;
    private boolean pixmapLoaded = false;
    public void createSprites(Pixmap from) {
        TextureManager.recolorRegion(from, item.color);
        pixmapRegion = TextureManager.alloc(from);
        pixmapLoaded = true;
    }

    public void reloadIcons() {
        createSprites(crafterSprites.get(size).random(size * 32, cr));
        applyIcons();
    }

    @Override
    public void createIcons(MultiPacker packer) {
        createSprites(crafterSprites.get(size).random(packer, size * 32, cr));
    }

    @Override
    public void load() {
        super.load();
        if (pixmapLoaded) applyIcons();
    }

    public void applyIcons() {
        region = fullIcon = uiIcon = pixmapRegion;
    }

    @Override
    public void loadIcon() {}

    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
        stats.add(tierStat, t -> t.add(Integer.toString(getTier())));
    }

    @Override
    public int getTier() {
        return id / 3 * 2 + 1;
    }

    @Override
    public TechTree.TechNode generateNode() {
        techNode = new TechTree.TechNode(
                last.size == 0 ? TechTree.context() : last.random(r).techNode,
                this,
                researchRequirements()
        );
        if (r.chance(0.5) && last.size > 0) last.remove(0);
        last.add(this);
        return techNode;
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
