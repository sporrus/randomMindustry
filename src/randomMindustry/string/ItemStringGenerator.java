package randomMindustry.string;

import arc.math.*;
import arc.struct.*;
import mindustry.type.*;

public class ItemStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(1)));
        out.append(generateSuffix());
        if(Mathf.chance(0.5f)) out.append(" ").append(generateType());
        return out.toString();
    }

    public String generateDescription(Item item){
        // talk about stats and stuff
        return "Casual item description.";
    }
    
    private String generateSuffix(){
        return Seq.with("ite", "ide", "ium").random();
    }
    
    private String generateType(){
        return Seq.with(
            "Alloy", "Fabric", "Compound", "Matter", "Pod", "Cyst",
            "Cluster", "Crystal", "Sheet", "Mix"
        ).random();
    }
}