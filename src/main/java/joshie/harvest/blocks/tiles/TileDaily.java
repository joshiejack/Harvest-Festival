package joshie.harvest.blocks.tiles;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.IDailyTickable;

public abstract class TileDaily extends TileHarvest implements IDailyTickable {
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
