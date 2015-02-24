package joshie.harvestmoon.items;

import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.core.util.base.ItemBaseMeta;

public abstract class ItemHMMeta extends ItemBaseMeta {
    public ItemHMMeta() {
        super(HMModInfo.MODPATH, HMTab.tabGeneral);
    }
}
