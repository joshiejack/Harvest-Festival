package joshie.harvestmoon.blocks;

import joshie.harvestmoon.HarvestTab;
import joshie.harvestmoon.lib.HMModInfo;
import joshie.lib.base.BlockBase;
import net.minecraft.block.material.Material;

public abstract class BlockHMBase extends BlockBase {
    protected BlockHMBase(Material material) {
        super(material, HMModInfo.MODPATH, HarvestTab.hm);
    }
}
