package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.core.lib.HMModInfo.MODPATH;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockWithered extends BlockHMBase {
    private IIcon[] icons;

    public BlockWithered() {
        super(Material.plants);
        setBlockUnbreakable();
        setStepSound(Block.soundTypeGrass);
        setCreativeTab(null);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 6;
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int side) {
        return null;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockz) {
        if (!world.isRemote) {
            world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canCollideCheck(int side, boolean boat) {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
        icons = new IIcon[3];
        icons[0] = register.registerIcon(MODPATH + ":withered_seeds");
        icons[1] = register.registerIcon(MODPATH + ":withered_growing");
        icons[2] = register.registerIcon(MODPATH + ":withered_grown");
    }
}
