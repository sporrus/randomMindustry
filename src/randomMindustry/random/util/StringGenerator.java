package randomMindustry.random.util;

public class StringGenerator {
    public static final String[] VOWELS = new String[]{
            "i", "e", "u", "a", "ee", "ea", "ie", "ou", "er", "or", "o", "ir", "ur", "ear", "our", "ar",
            "al", "au", "ae", "oar", "oo", "ui", "ew", "a", "eigh", "ow", "oa", "oi", "oy", "eer",
            "ere", "are", "air", "oor"
    };
    public static final String[] CONSONANTS = new String[]{
            "b", "c", "d", "f", "g", "h", "j", "k", "l", "m", "n", "p", "q", "r", "s", "t", "v", "w", "x",
            "y", "z"
    };
    public static final String[] TEMPLATE = new String[]{
            "cv", "vc", "cvc", "vcv"
    };

    public static String generateSyllable() {
        String template = RandomUtil.random(TEMPLATE, RandomUtil.getClientRand());
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < template.length(); i++) {
            if (template.charAt(i) == 'c') {
                out.append(RandomUtil.random(CONSONANTS, RandomUtil.getClientRand()));
            } else {
                out.append(RandomUtil.random(VOWELS, RandomUtil.getClientRand()));
            }
        }
        return out.toString();
    }

    public static String generateWord(int size) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < size; i++) out.append(generateSyllable());
        return out.toString();
    }

    public static String generateMaterialName() {
        StringBuilder out = new StringBuilder();
        if (RandomUtil.getClientRand().chance(0.5)) { // normal suffix
            out.append(capitalizeFirstLetter(generateWord(1)));
            out.append(generateMaterialSuffix());
        } else if (RandomUtil.getClientRand().chance(0.5)) { // type suffix
            out.append(capitalizeFirstLetter(generateWord(2)));
            out.append(" ").append(generateMaterialSuffixType());
        } else {
            out.append(capitalizeFirstLetter(generateWord(2)));
        }
        return out.toString();
    }
    
    public static String generateTurretName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(RandomUtil.getClientRand().random(1, 2))));
        out.append(generateTurretSuffix());
        
        return out.toString();
    }
    
    public static String generateDrillName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(1)));
        out.append(generateDrillSuffix());
        out.append(" Drill");
        
        return out.toString();
    }
    
    public static String generateCoreName() {
        StringBuilder out = new StringBuilder();
        out.append("Core: ");
        out.append(capitalizeFirstLetter(generateWord(RandomUtil.getClientRand().random(1, 2))));
        out.append(generateCoreSuffix());
        
        return out.toString();
    }
    
    public static String generateDrillSuffix() {
        return RandomUtil.random(new String[]{
                "pact", "act", "matic", "atic", "chal", "al", "ser", "er",
                "ption", "tion", "blast", "last", "ast", "tary", "pulse"
        }, RandomUtil.getClientRand());
    }
    
    public static String generateTurretSuffix() {
        return RandomUtil.random(new String[]{
                "tre", "re", "ter", "cer", "mer", "er", "orch", "rch",
                "lax", "ment", "ent", "rse", "ite", "te", "se", "le"
        }, RandomUtil.getClientRand());
    }
    
    public static String generateCoreSuffix() {
        return RandomUtil.random(new String[]{
                "ard", "rd", "ation", "tion", "cleus", "leus",
                "tadel", "adel", "del", "opolis", "polis", "olis",
                "cite", "ite", "uin", "crete", "rete" 
        }, RandomUtil.getClientRand());
    }

    public static String generateMaterialSuffix() {
        return RandomUtil.random(new String[]{
                "ite", "ium", "ide"
        }, RandomUtil.getClientRand());
    }

    public static String generateMaterialSuffixType() {
        return RandomUtil.random(new String[]{
                "Alloy", "Fabric", "Compound", "Matter", "Pod", "Cyst",
                "Clust", "Crystal", "Block", "Spore", "Mix"
        }, RandomUtil.getClientRand());
    }

    public static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
