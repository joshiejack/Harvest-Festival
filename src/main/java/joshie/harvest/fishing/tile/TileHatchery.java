package joshie.harvest.fishing.tile;

import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.core.base.render.RenderData;
import joshie.harvest.core.base.tile.TileSingleStack;
import joshie.harvest.core.helpers.SpawnItemHelper;
import joshie.harvest.core.helpers.StackHelper;
import joshie.harvest.fishing.FishingAPI;
import joshie.harvest.fishing.FishingHelper;
import joshie.harvest.fishing.HFFishing;
import joshie.harvest.fishing.block.BlockFloating.Floating;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TileHatchery extends TileSingleStack implements ITickable {
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.MAIN) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFFishing.FLOATING_BLOCKS && HFFishing.FLOATING_BLOCKS.getEnumFromState(state) == Floating.HATCHERY;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void newDay(World world, BlockPos pos, IBlockState state) {
            TileHatchery hatchery = (TileHatchery) world.getTileEntity(pos);
            if (hatchery.isOnWaterSurface(world, pos.down())) {
                ItemStack stack = hatchery.stack;
                if (stack != null && stack.stackSize < 10) {
                    int daysRequired = FishingAPI.INSTANCE.getDaysFor(stack);
                    if (daysRequired >= 1) {
                        hatchery.daysPassed++;
                        if (hatchery.daysPassed >= FishingAPI.INSTANCE.getDaysFor(stack)) {
                            hatchery.daysPassed = 0;
                            stack.stackSize++;
                            hatchery.saveAndRefresh();
                        }
                    }
                }
            }
        }
    };
    public final RenderData render = new RenderData();
    private List<ItemStack> renderStacks = new ArrayList<>();
    private int daysPassed = 0;
    private int breakChance = 100;
    private int last;

    @Override
    public DailyTickableBlock getTickableForTile() {
        return TICKABLE;
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        onFishChanged(); //Update the stack size
        render.doRenderUpdate(worldObj, pos, last);
    }

    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        onFishChanged(); //Update the stack size
        render.doRenderUpdate(worldObj, pos, last);
    }

    @Override
    public boolean onRightClicked(EntityPlayer player, ItemStack place) {
        int daysRequired = place == null ? 0 : FishingAPI.INSTANCE.getDaysFor(place);
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
            if (!worldObj.isRemote) {
                boolean broke = false;
                if (worldObj.rand.nextInt(100) > breakChance) {
                    IBlockState state = worldObj.getBlockState(getPos());
                    if (worldObj.setBlockToAir(getPos())) {
                        broke = true;
                        worldObj.playEvent(null, 2001, getPos(), Block.getStateId(state));
                    }
                } else breakChance -= 25;

                if (breakChance <= 0) breakChance = 0;
                if (stack.stackSize > 1 && !broke) {
                    SpawnItemHelper.spawnByEntity(player, StackHelper.toStack(stack, stack.stackSize - 1));
                    stack.stackSize = 1;
                } else {
                    SpawnItemHelper.spawnByEntity(player, stack);
                    stack = null;
                }

                saveAndRefresh();
            }

            return true;
        } else return false;
    }

    private void onFishChanged() {
        renderStacks.clear();
        if (stack != null) {
            for (int i = 0; i < stack.stackSize; i++) {
                renderStacks.add(StackHelper.toStack(stack, 1));
            }
        }

        last = renderStacks.size();
    }

    public List<ItemStack> getFish() {
        return renderStacks;
    }

    @Override
    public void update() {
        if (worldObj.isRemote) {
            render.rotate(worldObj);
        }
    }

    private boolean isOnWaterSurface(World world, BlockPos pos) {
        return FishingHelper.isWater(world, pos.east(), pos.west(), pos.north(), pos.south(), pos.east().south(), pos.east().north(), pos.west().north(), pos.west().south());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        daysPassed = nbt.getInteger("DaysPassed");
        breakChance = nbt.getByte("TimesPulled");
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("DaysPassed", daysPassed);
        nbt.setByte("TimesPulled", (byte) breakChance);
        return super.writeToNBT(nbt);
    }
}
