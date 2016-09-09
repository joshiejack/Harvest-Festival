package joshie.harvest.core.base.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.ticking.IDailyTickable;
import joshie.harvest.core.helpers.MCServerHelper;

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

    public void saveAndRefresh() {
        MCServerHelper.markForUpdate(worldObj, getPos(), 3);
        markDirty();
    }
}
