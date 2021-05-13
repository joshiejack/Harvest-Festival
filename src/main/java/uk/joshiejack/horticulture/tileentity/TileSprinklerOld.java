package uk.joshiejack.horticulture.tileentity;

import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader("sprinkler_old")
public class TileSprinklerOld extends TileSprinkler {
    public TileSprinklerOld() {
        super(0.5D, 1);
    }

    @Override
    protected double getRandomDouble() {
        return (world.rand.nextDouble() - 0.5D) / 3;
    }
}
