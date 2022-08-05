package randomMindustry.string;

import arc.math.*;
import arc.struct.*;
import mindustry.type.*;

public class ItemStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(1)));
        out.append(generateSuffix());
        if(r.rand.chance(0.5f)) out.append(" ").append(generateType());
        return out.toString();
    }

    public String generateDescription(Item item){
        StringBuilder out = new StringBuilder();
        out.append(Seq.with("Basic item.", "Casual item.").random());
        if(item.explosiveness > 0){
            out.append(" ");
            if(item.explosiveness < 0.25f){
                out.append("A little bit");
            }else if(item.explosiveness < 0.5f){
                out.append("Quite");
            }else if(item.explosiveness < 0.75f){
                out.append("Very");
            }else if(item.explosiveness < 1f){
                out.append("Dangerously");
            }else{
                out.append("Extremely, dangerously");
            }
            out.append(" explosive.");
        }
        if(item.radioactivity > 0) out.append(" It is " + (item.radioactivity < 0.5f ? "kind of" : "very") + " radioactive.");
        if(item.flammability > 0){
            out.append(" " + (item.flammability < 0.5f ? "A little bit" : "Oh lordy, it's very") + " flammable.");
            if(item.flammability >= 0.5f) out.append(" Must be handled with care.");
        }
        if(item.charge > 0) out.append(" This material is " + (item.charge < 0.5f ? "quite" : "very") + " conductive.");
        return out.toString();
    }
    
    private String generateSuffix(){
        return Seq.with("ite", "ide", "ium").random(r.rand);
    }
    
    private String generateType(){
        return Seq.with(
            "Alloy", "Fabric", "Compound", "Matter", "Pod", "Cyst",
            "Cluster", "Crystal", "Sheet", "Mix"
        ).random(r.rand);
    }
}
