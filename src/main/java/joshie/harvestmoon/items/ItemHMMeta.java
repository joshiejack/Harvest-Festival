package joshie.harvestmoon.items;

import joshie.harvestmoon.HarvestTab;
import joshie.harvestmoon.lib.HMModInfo;
import joshie.lib.base.ItemBaseMeta;

public abstract class ItemHMMeta extends ItemBaseMeta {
    public ItemHMMeta() {
        super(HMModInfo.MODPATH, HarvestTab.hm);
    }
}
