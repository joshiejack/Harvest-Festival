package joshie.harvestmoon.blocks;

import static joshie.harvestmoon.helpers.CropHelper.removeFarmland;
import static joshie.harvestmoon.lib.HMModInfo.MODPATH;
import joshie.harvestmoon.helpers.CropHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockSoil extends BlockFarmland {
    private IIcon mine_icon;
    public static final int MINE_HOE = 15;

    public BlockSoil() {
        super();
        setTickRandomly(false);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int side) {
        removeFarmland(world, x, y, z);
    }

    @Override
    public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion explosion) {
        removeFarmland(world, x, y, z);
    }

    @Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
        EnumPlantType type = plantable.getPlantType(world, x, y, z);
        return type == EnumPlantType.Crop || type == EnumPlantType.Plains;
    }

    @Override
    public void onFallenUpon(World world, int x, int y, int z, Entity entity, float f) {
        if (world.getBlockMetadata(x, y, z) == MINE_HOE) return; //No turning to dirt!
        else super.onFallenUpon(world, x, y, z, entity, f);
    }

    public static boolean hydrate(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        boolean ret = meta == 7 ? false : world.setBlockMetadataWithNotify(x, y, z, 7, 2);
        if (ret) {
            CropHelper.hydrate(world, x, y + 1, z);
        }

        return ret;
    }

    //Returns false if the soil is no longer farmland
    public static boolean dehydrate(World world, int x, int y, int z) {
        Block block = world.getBlock(x, y + 1, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (meta == MINE_HOE) {
            return true;
        } else if (block instanceof IPlantable && world.getBlock(x, y, z).canSustainPlant(world, x, y, z, ForgeDirection.UP, (IPlantable) block)) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            return true;
        } else if (meta == 7) {
            world.setBlockMetadataWithNotify(x, y, z, 0, 2);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isHydrated(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) instanceof BlockSoil && world.getBlockMetadata(x, y, z) == 7;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public IIcon getIcon(int side, int meta) {
        return meta == MINE_HOE? mine_icon: super.getIcon(side, meta);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister register) {
        super.registerBlockIcons(register);
        mine_icon = register.registerIcon(MODPATH + ":mine_hoe");
    }
}
