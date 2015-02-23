package joshie.harvestmoon.blocks;

import joshie.harvestmoon.HarvestTab;
import joshie.harvestmoon.base.BlockBase;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraft.block.material.Material;

public abstract class BlockHMBase extends BlockBase {
    protected BlockHMBase(Material material) {
        super(material, HMModInfo.MODPATH, HarvestTab.tabGeneral);
    }
}
