package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.block.material.Material;

public abstract class BlockHFBase extends BlockBase {
    protected BlockHFBase(Material material) {
        super(material, HFModInfo.MODPATH, HFTab.tabFarming);
    }
}
