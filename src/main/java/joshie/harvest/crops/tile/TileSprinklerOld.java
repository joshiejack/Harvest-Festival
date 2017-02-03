package joshie.harvest.crops.tile;

public class TileSprinklerOld extends TileSprinkler {
    public TileSprinklerOld() {
        super(0.5D, 1);
    }

    @Override
    protected double getRandomDouble() {
        return (worldObj.rand.nextDouble() - 0.5D) / 3;
    }
}
