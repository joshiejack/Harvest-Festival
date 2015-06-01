package joshie.harvest.blocks;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.lib.HFModInfo;
import joshie.harvest.core.util.base.BlockBaseMeta;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BlockHFBaseMeta extends BlockBaseMeta {
    protected BlockHFBaseMeta(Material material) {
        super(material, HFModInfo.MODPATH, HFTab.tabFarming);
    }
    
    protected BlockHFBaseMeta(Material material, CreativeTabs tab) {
        super(material, HFModInfo.MODPATH, tab);
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
