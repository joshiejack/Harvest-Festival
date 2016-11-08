package joshie.harvest.crops.tile;

public class TileSprinklerOld extends TileSprinkler {
    public TileSprinklerOld() {
        height = 0.5D;
        range = 1;
    }

    @Override
    public double getRandomDouble() {
        return (worldObj.rand.nextDouble() - 0.5D) / 3;
    }
}
