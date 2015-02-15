package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.helpers.CropHelper.destroyCrop;
import static joshie.harvestmoon.helpers.CropHelper.harvestCrop;

import java.util.Random;

import joshie.harvestmoon.crops.render.RenderCrop;
import joshie.harvestmoon.lib.RenderIds;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCrop extends BlockHMBase implements IPlantable {
    public BlockCrop() {
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
        return RenderIds.ALL;
    }

    @Override
    public Item getItemDropped(int meta, Random rand, int side) {
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) return false;
        else {
            return harvestCrop(player, world, x, y, z);
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block blockz) {
        if (!world.isRemote) {
            Block block = world.getBlock(x, y - 1, z);
            if (!block.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this)) {
                destroyCrop(null, world, x, y, z);
            }
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
        RenderCrop.registerIcons(register);
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return EnumPlantType.Crop;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return world.getBlock(x, y, z);
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return 0;
    }
}
