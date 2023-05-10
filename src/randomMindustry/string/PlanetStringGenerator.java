package randomMindustry.string;

public class PlanetStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(r.random(3, 6))));
        return out.toString();
    }
}
