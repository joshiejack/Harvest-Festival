package joshie.harvest.quests.block;

import joshie.harvest.core.base.tile.TileDaily;
import joshie.harvest.core.helpers.MCServerHelper;

public class TileQuestBoard extends TileDaily {
    @Override
    public Phase[] getPhases() {
        return new Phase[] { Phase.POST };
    }

    @Override
    public void newDay() {
        MCServerHelper.markTileForUpdate(this);
    }
}
