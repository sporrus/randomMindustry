package randomMindustry.string;

public class PlanetStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(r.random(2, 3))));
        return out.toString();
    }
}
