package joshie.harvest.core.util.base;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.helpers.generic.RegistryHelper;
import joshie.harvest.core.lib.HFModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public abstract class BlockHFBase extends Block {

    //General Constructor
    public BlockHFBase(Material material, CreativeTabs tab) {
        super(material);
        setCreativeTab(tab);
    }

    //Default to farming constructor
    public BlockHFBase(Material material) {
        this(material, HFTab.FARMING);
    }

    @Override
    public BlockHFBase setUnlocalizedName(String name) {
        super.setUnlocalizedName(name);
        RegistryHelper.registerBlock(this, name);
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return HFModInfo.MODID + "." + super.getUnlocalizedName();
    }
}