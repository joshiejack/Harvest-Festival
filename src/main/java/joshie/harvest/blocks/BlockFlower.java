package joshie.harvest.blocks;

import static net.minecraftforge.common.EnumPlantType.Plains;
import joshie.harvest.core.HFTab;
import joshie.harvest.core.util.base.BlockHFBaseMeta;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockFlower extends BlockHFBaseMeta implements IPlantable {
    public static final int GODDESS = 0;

    public BlockFlower() {
        super(Material.plants, HFTab.tabTown);
        float f = 0.2F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3.0F, 0.5F + f);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        return super.canPlaceBlockAt(world, x, y, z) && canBlockStay(world, x, y, z);
    }

    protected boolean canPlaceBlockOn(Block block) {
        return block == Blocks.grass || block == Blocks.dirt || block == Blocks.farmland;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
        checkAndDropBlock(world, x, y, z);
    }

    protected void checkAndDropBlock(World world, int x, int y, int z) {
        if (!canBlockStay(world, x, y, z)) {
            dropBlockAsItem(world, x, y, z, world.getBlockMetadata(x, y, z), 0);
            world.setBlock(x, y, z, getBlockById(0), 0, 2);
        }
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return world.getBlock(x, y - 1, z).canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, this);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, int x, int y, int z) {
        return Plains;
    }

    @Override
    public Block getPlant(IBlockAccess world, int x, int y, int z) {
        return this;
    }

    @Override
    public int getPlantMetadata(IBlockAccess world, int x, int y, int z) {
        return world.getBlockMetadata(x, y, z);
    }

    @Override
    public int getMetaCount() {
        return 1;
    }
}
