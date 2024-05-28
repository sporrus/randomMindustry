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
        if(consonants.contains(lastSyllable)){ //TODO: add complementing consonants
            String tmp = "vc";
        }else if(vowels.contains(lastSyllable)){
            String tmp = "cvc";
        }else{
            String tmp = initialTmp.random(r);
        }
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < tmp.length(); i++){
            if(tmp.charAt(i) == 'c'){
                String syllable = consonants.random(r);
            }else{
                String syllable = vowels.random(r);
            }
            out.append(syllable);
            lastSyllable = syllable;
        }
        return out.toString();
    }
    
    public String generateWord(int size){
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < size; i++) out.append(generateSyllable());
        lastSyllable = null;
        return out.toString();
    }
    
    public String upperCaseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }
}
