package joshie.harvestmoon.items;

import joshie.harvestmoon.HarvestTab;
import joshie.harvestmoon.base.ItemBaseMeta;
import joshie.harvestmoon.lib.HMModInfo;

public abstract class ItemHMMeta extends ItemBaseMeta {
    public ItemHMMeta() {
        super(HMModInfo.MODPATH, HarvestTab.tabGeneral);
    }
}
