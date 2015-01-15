package joshie.harvestmoon.blocks;

import joshie.harvestmoon.HarvestTab;
import joshie.harvestmoon.lib.HMModInfo;
import joshie.lib.base.BlockBaseMeta;
import net.minecraft.block.material.Material;

public abstract class BlockHMBaseMeta extends BlockBaseMeta {
    protected BlockHMBaseMeta(Material material) {
        super(material, HMModInfo.MODPATH, HarvestTab.hm);
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
