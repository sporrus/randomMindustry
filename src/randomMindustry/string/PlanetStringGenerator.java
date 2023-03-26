package randomMindustry.string;

public class PlanetStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(r.random(1, 2))));
        out.append(generateSuffix());
        if (r.chance(0.5)) out.append(" ").append(generateType());
        return out.toString();
    }
}
