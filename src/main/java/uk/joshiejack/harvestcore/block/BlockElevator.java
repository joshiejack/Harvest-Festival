package uk.joshiejack.harvestcore.block;

import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.harvestcore.network.mine.PacketDisplayElevatorGUI;
import uk.joshiejack.harvestcore.world.mine.MineHelper;
import uk.joshiejack.harvestcore.world.storage.SavedData;
import uk.joshiejack.penguinlib.block.base.BlockRotatableDouble;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockElevator extends BlockRotatableDouble<BlockElevator.Elevator> {
    public BlockElevator() {
        super(new ResourceLocation(HarvestCore.MODID, "elevator"), Material.WOOD, Elevator.class);
        setCreativeTab(CreativeTabs.DECORATIONS);
        setBlockUnbreakable();
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (isTop(state)) return onBlockActivated(world, pos.down(), world.getBlockState(pos.down()), player, hand, side, hitX, hitY, hitZ);
        BlockPos target = new BlockPos(player);
        if (target.equals(pos)) {
            if (!world.isRemote) {
                PenguinNetwork.sendToClient(new PacketDisplayElevatorGUI(SavedData.getMineData(world, world.provider.getDimension()).getMaxFloorForID(MineHelper.getMineIDFromEntity(player))), player);
            }

            return true;
        }

        return false;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    public enum Elevator implements IStringSerializable {
        BASIC;

        @Nonnull
        @Override
        public String getName() {
            return name().toLowerCase(Locale.ENGLISH);
        }
    }
}
