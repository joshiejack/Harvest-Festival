package joshie.harvest.blocks.tiles;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.core.IDailyTickable;
import joshie.harvest.core.helpers.generic.MCServerHelper;

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

    @Override
    public void onInvalidated() {}

    public void saveAndRefresh() {
        MCServerHelper.markForUpdate(worldObj, getPos(), 3);
        markDirty();
    }
}
