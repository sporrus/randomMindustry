package randomMindustry.string.block;

import arc.struct.*;
import randomMindustry.string.*;

public class DrillStringGenerator extends StringGenerator{
    public String generateName(int tier){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(r.random(1, 2))));
        if(r.chance(0.5f)){
            String last = String.valueOf(out.toString().charAt(out.toString().length() - 1));
            if(consonant(last)){
                Seq<String> picked = vowels.copy();
                picked.removeAll(avoidVowels.get(last));
                String append = picked.random(r);
                out.append(append);
            }
            out.append(generateSuffix());
        }
        out.append(" ").append(generateType(tier));
        return out.toString();
    }
    
    public String generateSuffix(){
        return Seq.with(
            "chanical", "trical", "blast", "ruption"
        ).random(r);
    }
    
    public String generateType(int tier){
        StringBuilder out = new StringBuilder();
        
        Seq<Seq<String>> types = Seq.with(
            Seq.with("drill", "bore"),
            Seq.with("driver", "auger"),
            Seq.with("piercer", "twister"),
            Seq.with("spinner", "whirler"),
            Seq.with("rotator", "extractor"),
            Seq.with("grinder", "carver"),
            Seq.with("shredder", "carver"),
            Seq.with("quaker", "rumbler"),
            Seq.with("demolisher", "devastator"),
            Seq.with("obliterator", "annihilator")
        );
        
        if(tier >= 3){
            if(r.chance(0.75f)){
                Seq<Seq<String>> tiers = Seq.with(
                    Seq.with("turbo", "mega"),
                    Seq.with("macro", "prime"),
                    Seq.with("maxi", "delta"),
                    Seq.with("gamma", "hyper"),
                    Seq.with("giga", "tera"),
                    Seq.with("exo", "exa"),
                    Seq.with("tita", "ultra"),
                    Seq.with("omega", "peta"),
                    Seq.with("omni", "infini")
                );
                
                out.append(tiers.get(tier).random(r));
            }else{
                String word = generateWord(2);
                String last = String.valueOf(word.charAt(word.length() - 1));
                if(consonant(last)){
                    Seq<String> picked = vowels.copy();
                    picked.removeAll(avoidVowels.get(last));
                    String letter = picked.random(r);
                    word += letter;
                }
                
                out.append(word);
            }
        }
        
        out.append(types.get(tier).random(r));
        return upperCaseFirst(out.toString());
    }
}
