package randomMindustry.string;

import arc.struct.*;
import randomMindustry.mappers.item.*;

public class ItemStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(r.random(1, 2))));
        out.append(generateSuffix());
        if (r.chance(0.5)) out.append(" ").append(generateType());
        return out.toString();
    }

    public String generateDescription(CustomItem item){
        return "Used in " + (r.chance(0.5) ? "advanced " : "") + Seq.with(
                "weaponry", "defense structures", "electronics", "bombs", "transportation",
                "units", "ammunition", "drills", "factories").random(r) + "." +
                " " + fraction(item.explosiveness) + " explosive." +
                " " + fraction(item.radioactivity) + " radioactive." +
                " " + fraction(item.flammability) + " flammable." +
                " " + fraction(item.charge) + " conductive.";
    }
    
    private String generateSuffix(){
        return Seq.with("ite", "ide", "ium", "ate", "unch").random(r);
    }
    
    private String generateType(){
        return Seq.with(
                "Alloy", "Fabric", "Matter", "Pod", "Cyst", "Cluster",
                "Crystal", "Sheet", "Mix", "Lattice", "Compound", "Plating",
                "Sheet", "Ingot", "Berry", "Metal", "Foil", "Block"
        ).random(r);
    }

    private String fraction(float frac) {
        return switch ((int) (frac * 6)) {
            case 0 -> "Not"; //skip instead
            case 1 -> "Slightly";
            case 2 -> "Sizeably";
            case 3 -> "Moderately";
            case 4 -> "Highly";
            case 5 -> "Extremely";
            default -> "Dangerously";
        };
    }
}
