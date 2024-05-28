package randomMindustry.string;

import arc.struct.*;
import randomMindustry.mappers.item.*;

public class ItemStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(2)));
        if(r.chance(0.5f)){
            String last = String.valueOf(out.toString().charAt(out.toString().length() - 1));
            if(vowel(last)){
                Seq<String> picked = consonants.copy();
                picked.removeAll(avoidConsonants.get(last));
                String append = picked.random(r);
                out.append(append);
            }
            out.append(generateSuffix());
        }
        if(r.chance(0.5f)) out.append(" ").append(generateType());
        return out.toString();
    }

    public String generateDescription(CustomItem item){
        return fraction(item.explosiveness) + " explosive." +
            " " + fraction(item.radioactivity) + " radioactive." +
            " " + fraction(item.flammability) + " flammable." +
            " " + fraction(item.charge) + " conductive.";
    }
    
    public String generateSuffix(){
        return Seq.with("ite", "ide", "ium", "ate", "unch").random(r);
    }
    
    public String generateType(){
        return Seq.with(
                "Alloy", "Fabric", "Matter", "Pod", "Cyst", "Cluster",
                "Crystal", "Sheet", "Mix", "Lattice", "Compound", "Plating",
                "Sheet", "Ingot", "Bar", "Metal", "Foil", "Block"
        ).random(r);
    }

    public String fraction(float frac) {
        return switch ((int) (frac * 6)) {
            case 0 -> "Not";
            case 1 -> "Slightly";
            case 2 -> "Sizeably";
            case 3 -> "Moderately";
            case 4 -> "Highly";
            case 5 -> "Extremely";
            default -> "Dangerously";
        };
    }
}
