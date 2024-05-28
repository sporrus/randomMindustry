package randomMindustry.string;

import arc.struct.*;
import arc.util.*;
import randomMindustry.random.*;

public class StringGenerator{
    protected @Nullable String lastLetter;
    protected SyncedRand r = new SyncedRand();

    public final Seq<String> vowels = Seq.with(
        "a", "e", "i", "o", "u"
    );
    
    public final Seq<String> consonants = Seq.with(
        "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x",
        "y", "z"
    );
    
    public final ObjectMap<String, String> avoidConsonants = ObjectMap.with(
        "a", Seq.with("j", "q", "x", "z"),
        "e", Seq.with("j", "q", "x", "z"),
        "i", Seq.with("j", "q", "x", "z"),
        "o", Seq.with("j", "q", "x", "z"),
        "u", Seq.with("j", "q", "x", "z")
    );
    
    public final ObjectMap<String, String> avoidVowels = ObjectMap.with(
        "c", Seq.with("e", "i"),
        "g", Seq.with("e", "i"),
        "h", Seq.with("u"),
        "j", Seq.with("e", "i"),
        "k", Seq.with("e"),
        "q", Seq.with("e", "i"),
        "v", Seq.with("o", "u"),
        "w", Seq.with("i"),
        "y", Seq.with("i"),
        "z", Seq.with("a")
    );

    public final Seq<String> initialTmp = Seq.with(
        "cvc", "vcv", "cv", "vc", "c", "v"
    );

    public String generateSyllable(){
        String tmp;
        if(consonant(lastLetter)){ //TODO: add complementing consonants
            tmp = "vc";
        }else if(vowel(lastLetter)){
            tmp = "cvc";
        }else{
            tmp = initialTmp.random(r);
        }
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < tmp.length(); i++){
            String letter;
            if(tmp.charAt(i) == 'c'){
                Seq<String> picked = consonants.copy();
                if(avoidConsonants.containsKey(lastLetter)){
                    picked.removeAll(avoidConsonants.get(lastLetter));
                }
                letter = picked.random(r);
            }else{
                Seq<String> picked = vowels.copy()
                if(avoidVowels.containsKey(lastLetter)){
                    picked.removeAll(avoidVowels.get(lastLetter));
                }
                letter = picked.random(r);
            }
            out.append(letter);
            lastLetter = letter;
        }
        return out.toString();
    }
    
    public String generateWord(int size){
        StringBuilder out = new StringBuilder();
        for(int i = 0; i < size; i++) out.append(generateSyllable());
        lastLetter = null;
        return out.toString();
    }
    
    public String upperCaseFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public boolean consonant(String l){
        return consonants.contains(l);
    }

    public boolean vowel(String l){
        return vowels.contains(l);
    }
}
