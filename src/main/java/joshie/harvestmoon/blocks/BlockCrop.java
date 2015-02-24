package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.core.helpers.CropHelper.destroyCrop;
import static joshie.harvestmoon.core.helpers.CropHelper.harvestCrop;

import java.util.Random;

import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.helpers.CropHelper;
import joshie.harvestmoon.core.helpers.generic.MCClientHelper;
import joshie.harvestmoon.core.lib.RenderIds;
import joshie.harvestmoon.crops.Crop;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
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
        setBlockBounds(0F, 0F, 0F, 1F, 0.25F, 1F);
        setTickRandomly(Crops.ALWAYS_GROW);
        disableStats();
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
        return RenderIds.CROPS;
    }

    //Only called if crops are set to tick randomly
    @Override
    public void updateTick(World world, int x, int y, int z, Random rand) {
        if (Crops.ALWAYS_GROW) {
            if (!world.isRemote) {
                if (world.getBlockLightValue(x, y + 1, z) >= 9) {
                    float chance = getGrowthChance(world, x, y, z);
                    if (rand.nextInt((int) (25.0F / chance) + 1) == 0) {
                        //We are Growing!
                        CropHelper.grow(world, x, y, z);
                    }
                }
            }
        }
    }

    private float getGrowthChance(World world, int x, int y, int z) {
        float f = 1.0F;
        Block block = world.getBlock(x, y, z - 1);
        Block block1 = world.getBlock(x, y, z + 1);
        Block block2 = world.getBlock(x - 1, y, z);
        Block block3 = world.getBlock(x + 1, y, z);
        Block block4 = world.getBlock(x - 1, y, z - 1);
        Block block5 = world.getBlock(x + 1, y, z - 1);
        Block block6 = world.getBlock(x + 1, y, z + 1);
        Block block7 = world.getBlock(x - 1, y, z + 1);
        boolean xTrue = block2 == this || block3 == this;
        boolean zTrue = block == this || block1 == this;
        boolean cornerTrue = block4 == this || block5 == this || block6 == this || block7 == this;

        for (int l = x - 1; l <= x + 1; ++l) {
            for (int i1 = z - 1; i1 <= z + 1; ++i1) {
                float f1 = 0.0F;

                if (world.getBlock(l, y - 1, i1).canSustainPlant(world, l, y - 1, i1, ForgeDirection.UP, this)) {
                    f1 = 1.0F;

                    if (world.getBlock(l, y - 1, i1).isFertile(world, l, y - 1, i1)) {
                        f1 = 3.0F;
                    }
                }

                if (l != x || i1 != z) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (cornerTrue || xTrue && zTrue) {
            f /= 2.0F;
        }

        return f;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        return CropHelper.getIconForCrop(MCClientHelper.getWorld(), x, y, z);
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
    public void registerBlockIcons(IIconRegister register) {
        for (Crop crop : Crop.crops) {
            crop.registerIcons(register);
        }
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
