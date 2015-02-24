package joshie.harvestmoon.blocks;

import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.core.util.base.BlockBase;
import net.minecraft.block.material.Material;

public abstract class BlockHMBase extends BlockBase {
    protected BlockHMBase(Material material) {
        super(material, HMModInfo.MODPATH, HMTab.tabGeneral);
    }
}
