package randomMindustry.string;

import arc.math.*;
import arc.struct.*;
import randomMindustry.*;

public class StringGenerator{
    protected SyncedRand r;

    public final Seq<String> vowels = Seq.with(
        "i", "e", "u", "a", "ee", "ea", "ie", "ou", "er", "or", "o", "ir", "ur", "ear", "our", "ar",
        "al", "au", "ae", "oar", "oo", "ui", "ew", "a", "eigh", "ow", "oa", "oi", "oy", "eer",
        "ere", "are", "air", "oor"
    );
    
    public final Seq<String> consonants = Seq.with(
        "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x",
        "y", "z"
    );

    public StringGenerator(){
        r = new SyncedRand();
    }
    
    public final Seq<String> template = Seq.with("cv", "vc", "cvc", "vcv");
    
    public String generateSyllable(){
        String tmp = template.random(r);
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < tmp.length(); i++){
            if(tmp.charAt(i) == 'c') out.append(consonants.random(r));
            else out.append(vowels.random(r));
        }
        return out.toString();
    }
    
    public String generateWord(int size){
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < size; i++) out.append(generateSyllable());
        return out.toString();
    }
    
    public String upperCaseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
