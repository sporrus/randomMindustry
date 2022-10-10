package randomMindustry.string;

import arc.struct.Seq;
import randomMindustry.mappers.item.CustomItem;

public class ItemStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(1)));
        out.append(generateSuffix());
        if(r.chance(0.5f)) out.append(" ").append(generateType());
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
        return Seq.with("ite", "ide", "ium").random(r);
    }
    
    private String generateType(){
        return Seq.with(
            "Alloy", "Fabric", "Compound", "Matter", "Pod", "Cyst",
            "Cluster", "Crystal", "Sheet", "Mix"
        ).random(r);
    }

    private String fraction(float frac) {
        return switch ((int) (frac * 6)) {
            case 0 -> "Not";
            case 1 -> "A bit";
            case 2 -> "Kinda";
            case 3 -> "Quite";
            case 4 -> "Very";
            case 5 -> "Extremely";
            default -> "Dangerously";
        };
    }
}
