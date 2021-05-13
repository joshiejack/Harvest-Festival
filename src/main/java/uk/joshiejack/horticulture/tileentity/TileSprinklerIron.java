package uk.joshiejack.horticulture.tileentity;

import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader("sprinkler_iron")
public class TileSprinklerIron extends TileSprinkler {
    public TileSprinklerIron() {
        super(0.7D, 4);
    }

    @Override
    protected double getRandomDouble() {
        return world.rand.nextDouble() - 0.5D;
    }
}
