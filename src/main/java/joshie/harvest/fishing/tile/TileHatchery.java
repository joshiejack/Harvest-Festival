package joshie.harvest.fishing.tile;

import joshie.harvest.core.base.tile.TileSingleStack;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.fishing.FishingAPI;
import joshie.harvest.fishing.FishingHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileHatchery extends TileSingleStack {
    private int daysPassed = 0;

    @Override
    public boolean onRightClicked(EntityPlayer player, ItemStack place) {
        int daysRequired = FishingAPI.INSTANCE.getDaysFor(place);
        if (daysRequired <= 0) return removeFish(player);
        if (stack != null && (!stack.isItemEqual(place) || stack.stackSize >= 10)) return false;
        else {
            ItemStack single = place.splitStack(1);
            if (stack == null) {
                stack = single.copy();
            } else stack.stackSize++;

            saveAndRefresh();
            return true;
        }
    }

    private boolean removeFish(EntityPlayer player) {
        if (stack != null) {
            SpawnItemHelper.addToPlayerInventory(player, stack);
            if (worldObj.rand.nextInt(4) == 0) {
                IBlockState state = worldObj.getBlockState(getPos());
                worldObj.setBlockToAir(getPos());
                worldObj.playEvent(null, 2001, getPos(), Block.getStateId(state));
            }

            return true;
        } else return false;
    }

    @Override
    public void newDay() {
        if (isOnWaterSurface(worldObj, getPos())) {
            if (stack != null && stack.stackSize < 10) {
                int daysRequired = FishingAPI.INSTANCE.getDaysFor(stack);
                if (daysRequired >= 1) {
                    daysPassed++;
                    if (daysPassed >= FishingAPI.INSTANCE.getDaysFor(stack)) {
                        daysPassed = 0;
                        stack.stackSize++;
                    }
                }
            }
        }
    }

    private boolean isOnWaterSurface(World world, BlockPos pos) {
        return world.isAirBlock(pos.up()) && FishingHelper.isWater(world, pos.down(), pos.east(), pos.west(), pos.north(), pos.south());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        daysPassed = nbt.getInteger("DaysPassed");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("DaysPassed", daysPassed);
        return super.writeToNBT(nbt);
    }
}
