package joshie.harvestmoon.blocks;

import joshie.harvestmoon.HarvestTab;
import joshie.harvestmoon.base.BlockBaseMeta;
import joshie.harvestmoon.lib.HMModInfo;
import net.minecraft.block.material.Material;

public abstract class BlockHMBaseMeta extends BlockBaseMeta {
    protected BlockHMBaseMeta(Material material) {
        super(material, HMModInfo.MODPATH, HarvestTab.tabGeneral);
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
