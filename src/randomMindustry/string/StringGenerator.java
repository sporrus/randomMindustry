package randomMindustry.string;

import arc.struct.*;
import arc.util.*;
import randomMindustry.random.*;

public class StringGenerator{
    protected @Nullable String lastSyllable;
    protected SyncedRand r = new SyncedRand();

    public final Seq<String> vowels = Seq.with(
        "a", "e", "i", "o", "u"
    );
    
    public final Seq<String> consonants = Seq.with(
        "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x",
        "y", "z"
    );

    public final Seq<String> initialTmp = Seq.with(
        "cvc", "vcv", "cv", "vc", "c", "v"
    );

    public String generateSyllable(){
        String tmp;
        if(consonants.contains(lastSyllable)){ //TODO: add complementing consonants
            tmp = "vc";
        }else if(vowels.contains(lastSyllable)){
            tmp = "cvc";
        }else{
            tmp = initialTmp.random(r);
        }
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
        lastSyllable = false;
        return out.toString();
    }
    
    public String upperCaseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
