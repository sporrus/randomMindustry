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
    public static final String[] ODDS = new String[]{
            "1", "3", "5", "7", "9"
    };
    public static final String[] EVENS = new String[]{
            "0", "2", "4", "6", "8"
    };
    public static final String[] TEMPLATE = new String[]{
            "cv", "vc", "cvc", "vcv"
    };
    public static final String[] TEMPLATENUM = new String[]{
            "eo", "oe", "eoe", "oeo", "oee", "ooe"
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
    
    public static String generateNumber(int size) {
        String template = RandomUtil.random(TEMPLATENUM, RandomUtil.getClientRand());
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < template.length(); j++) {
                if (template.charAt(i) == 'e') {
                    out.append(RandomUtil.random(EVENS, RandomUtil.getClientRand()));
                } else {
                    out.append(RandomUtil.random(ODDS, RandomUtil.getClientRand()));
                }
            }
        }
        return out.toString(); 
    }

    public static String generateMaterialName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(1))).append(generateMaterialSuffix());
        if (RandomUtil.getClientRand().chance(0.5))
            out.append(" ").append(generateMaterialSuffixType());
        return out.toString();
    }

    public static String generateMaterialSuffix() {
        return RandomUtil.random(new String[]{
                "ite", "ium", "ide", "ast", "ant", "ten"
        }, RandomUtil.getClientRand());
    }

    public static String generateMaterialSuffixType() {
        return RandomUtil.random(new String[]{
                "Alloy", "Fabric", "Compound", "Matter", "Pod", "Cyst",
                "Clust", "Crystal", "Block", "Sphere", "Spore", "Mix"
        }, RandomUtil.getClientRand());
    }

    public static String generateTurretName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(1)));
        out.append(generateTurretSuffix());
        return out.toString();
    }

    public static String generateTurretSuffix() {
        return RandomUtil.random(new String[]{
                "tre", "re", "ter", "cer", "mer", "er", "orch", "rch",
                "lax", "ment", "ent", "rse", "ite", "te", "se", "le"
        }, RandomUtil.getClientRand());
    }

    public static String generateDrillName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(1)));
        out.append(generateDrillSuffix());
        out.append(" Drill");
        return out.toString();
    }

    public static String generateDrillSuffix() {
        return RandomUtil.random(new String[]{
                "pact", "act", "matic", "atic", "chal", "al", "ser", "er",
                "ption", "tion", "blast", "last", "ast", "tary", "pulse"
        }, RandomUtil.getClientRand());
    }

    public static String generateCoreName() {
        StringBuilder out = new StringBuilder();
        out.append("Core: ");
        out.append(capitalizeFirstLetter(generateWord(RandomUtil.getClientRand().random(1, 2))));
        out.append(generateCoreSuffix());
        return out.toString();
    }

    public static String generateCoreSuffix() {
        return RandomUtil.random(new String[]{
                "ard", "rd", "ation", "tion", "cleus", "leus",
                "tadel", "adel", "del", "opolis", "polis", "olis",
                "cite", "ite", "uin", "crete", "rete"
        }, RandomUtil.getClientRand());
    }

    public static String generateUnitName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(1)));
        out.append(generateUnitSuffix());
        return out.toString();
    }

    public static String generateUnitSuffix() {
        return RandomUtil.random(new String[]{
                "ger", "ace", "ress", "pter", "eign",
                "va", "lsar", "asar", "la", "vus",
                "ler", "rax", "roct", "yid", "pid",
                "are", "zon", "ith", "ra", "ipse",
                "no", "ly", "ga", "ad", "ct",
                "sso", "ke", "yde", "ei", "ura",
                "usa", "noe", "erce", "ires", "nax",
                "pha", "eta", "mma",
                "ell", "cus", "ept", "ish", "quer",
                "rui", "roi", "us", "cta", "aris",
                "ude", "vert", "ate", "ll", "upt",
                "oke", "cite", "nate"
        }, RandomUtil.getClientRand());
    }
    
    public static String generateUnitFactoryName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(1)));
        out.append(generateUnitFactorySuffix());
        out.append(" " + RandomUtil.random(new String[]{ "Fabricator", "Constructor", "Builder", "Factory", "Assembler" }, RandomUtil.getClientRand()));
        return out.toString();
    }
    
    public static String generateUnitFactorySuffix() {
        return RandomUtil.random(new String[]{
                "ound", "und", "nd", "ir", "val", "al", "ank", "nk", "ip",
                "ech", "ch", "on", "ron", "tron", "ute", "nide", "ide"
        }, RandomUtil.getClientRand());
    }
    
    public static String generateReconstructorName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(1)));
        out.append(generateReconstructorSuffix());
        out.append(" " + RandomUtil.random(new String[]{ "Refabricator", "Reconstructor" }, RandomUtil.getClientRand()));
        return out.toString();
    }
    
    public static String generateReconstructorSuffix() {
        return RandomUtil.random(new String[]{
                "ditive", "cative", "nential", "ential", "tial", "trative",
                "itive", "ative", "rative", "tive"
        }, RandomUtil.getClientRand());
    }
    
    public static String generateSectorName() {
        StringBuilder out = new StringBuilder();
        out.append(capitalizeFirstLetter(generateWord(2)));
        if (RandomUtil.getClientRand().chance(0.75f)) {
            out.append(" " + generateSectorPlace());
            if (RandomUtil.getClientRand().chance(0.25f)) out.append(" " + generateNumber(1));
        }
        return out.toString();
    }
    
    public static String generateSectorPlace() {
        return RandomUtil.random(new String[]{
                "Market", "Zero", "Facility", "Craters", "Canyons", "Floors", "Mountains",
                "Meteor", "Ruins", "Shores", "Bunker", "Flats"
        }, RandomUtil.getClientRand());
    }

    public static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
