package joshie.harvestmoon.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDirt extends BlockHMBaseMeta {
    public BlockDirt() {
        super(Material.ground);
    }

    @Override
    public int getMetaCount() {
        return 16;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        String name = prefix != null ? prefix : "";
        icons = new IIcon[1];
        icons[0] = iconRegister.registerIcon(mod + ":" + name + getName(0));
    }
}
