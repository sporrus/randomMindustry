package randomMindustry.string;

public class PlanetStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(r.random(1, 2))));
        return out.toString();
    }
}
