package harvestmoon.blocks;

import static harvestmoon.lib.ModInfo.MODPATH;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockBase extends Block {
    protected BlockBase(Material material) {
        super(material);
    }
    
    @Override
    public BlockBase setBlockName(String name) {
        return (BlockBase) super.setBlockName(name);
    }
    
    public Block register() {
        GameRegistry.registerBlock(this, super.getUnlocalizedName());
        return this;
    }

    @Override
    public String getUnlocalizedName() {
        return MODPATH + "." + super.getUnlocalizedName();
    }
}
