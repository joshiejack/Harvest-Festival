package joshie.harvestmoon.blocks;

import joshie.harvestmoon.core.HMTab;
import joshie.harvestmoon.core.lib.HMModInfo;
import joshie.harvestmoon.core.util.base.BlockBaseMeta;
import net.minecraft.block.material.Material;

public abstract class BlockHMBaseMeta extends BlockBaseMeta {
    protected BlockHMBaseMeta(Material material) {
        super(material, HMModInfo.MODPATH, HMTab.tabGeneral);
    }
    
    @Override
    public String getToolType(int meta) {
        return "pickaxe";
    }

    @Override
    public int getToolLevel(int meta) {
        return 0;
    }
}
