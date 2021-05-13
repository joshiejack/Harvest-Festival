package uk.joshiejack.piscary.tile;

import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetInventorySlot;
import uk.joshiejack.penguinlib.tile.inventory.TileSingleStack;
import uk.joshiejack.penguinlib.client.renderer.tile.SpecialRenderData;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TerrainHelper;
import uk.joshiejack.piscary.database.HatcheryRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

@PenguinLoader("hatchery")
public class TileHatchery extends TileSingleStack implements ITickable {
    public final SpecialRenderData render = new SpecialRenderData();
    private int daysPassed = 0;
    private int breakChance = 125;
    private int last;

    public SpecialRenderData getRenderData() {
        return render;
    }

    @Override
    public boolean isStackValidForInsertion(int slot, ItemStack stack) {
        return HatcheryRegistry.getValue(stack) >= 1 && handler.getStackInSlot(0).isEmpty();
    }

    @Override
    public boolean isSlotValidForExtraction(int slot, int amount) {
        return handler.getStackInSlot(0).getCount() - amount > 1;
    }

    @Override
    public void updateTick() {
        ItemStack fish = handler.getStackInSlot(0);
        if (!fish.isEmpty() && fish.getCount() < 10 && isOnWaterSurface(world, pos.down())) {
            int days = HatcheryRegistry.getValue(fish);
            if (days >= 1) {
                daysPassed++;
                if (daysPassed >= days) {
                    daysPassed = 0; //Reset
                    fish.grow(1);
                    onFishChanged();
                }
            }
        }
    }

    private void onFishChanged() {
        if (!world.isRemote) {
            PenguinNetwork.sendToNearby(this, new PacketSetInventorySlot(pos, 0, handler.getStackInSlot(0)));
        } else last = handler.getStackInSlot(0).getCount();
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        super.onDataPacket(net, packet);
        render.doRenderUpdate(world, pos, last);
    }

    @Override
    public void handleUpdateTag(@Nonnull NBTTagCompound tag) {
        super.handleUpdateTag(tag);
        render.doRenderUpdate(world, pos, last);
    }


    @Override
    public boolean onRightClicked(EntityPlayer player, EnumHand hand) {
        ItemStack held = player.getHeldItem(hand);
        ItemStack internal = handler.getStackInSlot(0);
        if (HatcheryRegistry.getValue(held) >= 1 && internal.isEmpty()) {
            //If this is a fish then we'll attempt to insert it if the hatchery is empty
            ItemStack fish = held.copy();
            held.shrink(1); //Remove the held item by one
            fish.setCount(1); //We only want one fish
            handler.setStackInSlot(0, fish); //We've inserted one fish
            onFishChanged();
            return true;
        } else if (!internal.isEmpty()) {
            if (!world.isRemote) {
                //Now we know we're not inserting a fish, so we want to take out any fish inside
                //First step is to take out all the fish, except one
                if (internal.getCount() > 1 && !attemptToDestroyHatchery()) { //Give all the fish if the hatchery breaks
                    ItemStack fish = internal.copy();
                    fish.shrink(1); //Don't take all the fish
                    onFishChanged();
                    ItemHandlerHelper.giveItemToPlayer(player, fish); //Give them the fish
                } else {
                    ItemHandlerHelper.giveItemToPlayer(player, internal);
                    handler.setStackInSlot(0, ItemStack.EMPTY); //Mark it as empty
                    onFishChanged();
                }
            }

            return true;
        }

        return false;
    }

    private boolean attemptToDestroyHatchery() {
        if (world.rand.nextInt(100) > breakChance) {
            IBlockState state = world.getBlockState(pos);
            world.setBlockToAir(pos);
            world.playEvent(null, 2001, getPos(), Block.getStateId(state));
            return true;
        } else breakChance -= 25;

        breakChance = MathsHelper.constrainToRangeInt(breakChance, 0, 100);
        return false;
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getStack() {
        return handler.getStackInSlot(0);
    }
    @Override
    public void update() {
        if (world.isRemote) {
            render.rotate(world);
        }
    }

    private boolean isOnWaterSurface(World world, BlockPos pos) {
        return TerrainHelper.isWater(world, pos.east(), pos.west(), pos.north(), pos.south(), pos.east().south(), pos.east().north(), pos.west().north(), pos.west().south());
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
