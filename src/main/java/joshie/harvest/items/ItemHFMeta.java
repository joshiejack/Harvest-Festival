package joshie.harvest.items;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.ItemBaseMeta;
import net.minecraft.creativetab.CreativeTabs;

public abstract class ItemHFMeta extends ItemBaseMeta {
    public ItemHFMeta() {
        super(HFModInfo.MODPATH, HFTab.FARMING);
    }
    
    public ItemHFMeta(CreativeTabs tab) {
        super(HFModInfo.MODPATH, tab);
    }
}
