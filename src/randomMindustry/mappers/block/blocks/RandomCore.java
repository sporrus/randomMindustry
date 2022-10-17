package randomMindustry.mappers.block.blocks;

import arc.struct.*;
import mindustry.content.*;
import mindustry.type.*;
import mindustry.world.blocks.storage.*;
import randomMindustry.*;
import randomMindustry.mappers.item.*;

import static randomMindustry.mappers.block.BlockMapper.r;

public class RandomCore extends CoreBlock implements RandomBlock{
    public final int id;
    public static final int maxTier = 4;
    public static final ObjectMap<Integer, Seq<UnitType>> types = ObjectMap.of(
        1, Seq.with(UnitTypes.alpha, UnitTypes.beta),
        2, Seq.with(UnitTypes.poly, UnitTypes.gamma),
        3, Seq.with(UnitTypes.mega),
        4, Seq.with(UnitTypes.oct)
    );
    
    public RandomCore(String name, int id){
        super(name + id);
        this.id = id;
        size = id + 3;
        generate();
    }
    
    @Override
    public int getTier(){
        return id + 1;
    }
    
    @Override
    public void setStats() {
        super.setStats();
        stats.add(RMVars.seedStat, RMVars.seedStatValue);
    }
    
    @Override
    public void reload(){
        generate();
    }
    
    public void generate(){
        if(id == 0){
            isFirstTier = alwaysUnlocked = true;
        }
        requirements(Category.effect, ItemMapper.getItemStacks(id, r.random(3, 5) + id, () -> (1000 + (getTier() * 200)) + r.random(80, 300)));
        unitType = types.get(getTier()).random(r);
        health = (2000 + (getTier() * 1500)) + r.random(150, 550);
        armor = id * 2;
        itemCapacity = (2500 * getTier()) + r.random(150, 550);
        unitCapModifier = 15 * getTier();
    }
}
