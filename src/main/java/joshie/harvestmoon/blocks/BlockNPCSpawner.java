package joshie.harvestmoon.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockNPCSpawner extends BlockHMBaseMeta {
    public BlockNPCSpawner() {
        super(Material.iron);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        return;
    }

    @Override
    public int getMetaCount() {
        return 1;
    }
}
