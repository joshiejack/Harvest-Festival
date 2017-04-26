package joshie.harvest.mining.item;

import joshie.harvest.buildings.BuildingHelper;
import joshie.harvest.core.base.item.ItemBlockHF;
import joshie.harvest.core.helpers.TextHelper;
import joshie.harvest.core.lib.CreativeSort;
import joshie.harvest.core.util.interfaces.IFaceable;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.block.BlockElevator;
import joshie.harvest.mining.block.BlockElevator.Elevator;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockElevator extends ItemBlockHF {
    @SuppressWarnings("unchecked")
    public ItemBlockElevator(BlockElevator elevator) {
        super(elevator);
        setMaxStackSize(1);
    }

    private boolean isMineWall(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == HFMining.STONE;
    }

    private boolean isMineFloor(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == HFMining.DIRT;
    }

    private boolean hasMineFloorBelow(World world, BlockPos pos) {
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            if (isMineFloor(world, pos.offset(facing).down())) return true;
        }

        return false;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.provider.getDimension() == HFMining.MINING_ID) {
            RayTraceResult raytrace = BuildingHelper.rayTrace(player, 5D, 0F);
            if (raytrace == null || (raytrace.sideHit == EnumFacing.DOWN || raytrace.sideHit == EnumFacing.UP)) return new ActionResult<>(EnumActionResult.PASS, stack); //We didn't ind what we want
            BlockPos pos = raytrace.getBlockPos();
            if (isMineWall(world, pos) && isMineWall(world, pos.up()) && hasMineFloorBelow(world, pos)) {
                if (!world.isRemote) {
                    world.setBlockState(pos, HFMining.ELEVATOR.getStateFromEnum(Elevator.JUNK));
                    world.setBlockState(pos.up(), HFMining.ELEVATOR.getStateFromEnum(Elevator.EMPTY));
                    world.setBlockState(pos.up(2), Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, EnumType.DARK_OAK));
                    world.setBlockState(pos.up(2).offset(raytrace.sideHit), Blocks.WALL_SIGN.getDefaultState().withProperty(BlockWallSign.FACING, raytrace.sideHit));
                    TileEntity tile = world.getTileEntity(pos);
                    if (tile instanceof IFaceable) {
                        ((IFaceable) tile).setFacing(raytrace.sideHit);
                    }

                    TileEntity tile2 = world.getTileEntity(pos.up(2).offset(raytrace.sideHit));
                    if (tile2 instanceof TileEntitySign) {
                        TileEntitySign sign = ((TileEntitySign) tile2);
                        sign.signText[1] = new TextComponentTranslation("harvestfestival.elevator.to");
                        sign.signText[2] = new TextComponentTranslation("harvestfestival.elevator.none");
                        sign.markDirty();
                        IBlockState state = world.getBlockState(sign.getPos());
                        world.notifyBlockUpdate(sign.getPos(), state, state, 3);
                    }
                }

                stack.shrink(1); //Reduce the stack size
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(@Nonnull EntityPlayer playerIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public int getSortValue(@Nonnull ItemStack stack) {
        return CreativeSort.LAST;
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable EntityPlayer player, @Nullable List<String> tooltip, boolean advanced) {
        tooltip.add(TextFormatting.AQUA + TextHelper.translate("elevator.place"));
    }
}
