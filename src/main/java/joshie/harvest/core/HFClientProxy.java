package joshie.harvest.core;

import joshie.harvest.core.util.base.ItemBlockHF;
import net.minecraft.item.Item;

public class HFClientProxy extends HFCommonProxy {

    @Override
    public boolean isClient() {
        return true;
    }

    public void setBlockModelResourceLocation(Item item, String name) {
        name = name.replace(".", "_");
        if (item instanceof ItemBlockHF) {
            ((ItemBlockHF)item).getBlock().registerModels(item, name);
        }
    }
}
