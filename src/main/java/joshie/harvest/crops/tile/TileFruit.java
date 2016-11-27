package joshie.harvest.crops.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.core.base.tile.TileHarvest;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockFruit.Fruit;

public class TileFruit extends TileHarvest implements IDailyTickable {
    @Override
    public Phase[] getPhases() {
        return new Phase[] { Phase.LAST };
    }

    @Override
    public void newDay() {
        Fruit fruit = HFCrops.FRUITS.getEnumFromState(worldObj.getBlockState(pos));
        Season season = fruit.getCrop().getSeasons()[0];
        if (HFApi.calendar.getDate(worldObj).getSeason() != season) {
            worldObj.setBlockToAir(pos);
        }
    }

    @Override
    public void validate() {
        super.validate();
        //Update the ticker
        HFApi.tickable.addTickable(worldObj, this);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        //Update the ticker
        HFApi.tickable.removeTickable(worldObj, this);
    }
}
