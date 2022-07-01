package randomMindustry.util.techTrees;

import arc.struct.*;
import mindustry.*;
import mindustry.ctype.*;
import mindustry.type.*;

public class TechUtil {
    public static Seq<Planet> getRoot(UnlockableContent content) {
        Seq<Planet> planets = new Seq<>();
        Vars.content.planets().each((planet -> {
            if (planet.techTree == null) return;
            planet.techTree.each((node) -> {
                if (node.content == content) {
                    planets.add(planet);
                }
            });
        }));
        return planets;
    }
}
