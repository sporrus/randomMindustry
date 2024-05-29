package randomMindustry.string;

public class PlanetStringGenerator extends StringGenerator{
    public String generateName(){
        StringBuilder out = new StringBuilder();
        out.append(upperCaseFirst(generateWord(2)));
        return out.toString();
    }
}
