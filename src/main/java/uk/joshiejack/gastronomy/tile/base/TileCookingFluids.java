package uk.joshiejack.gastronomy.tile.base;

import uk.joshiejack.gastronomy.api.Appliance;
import uk.joshiejack.penguinlib.block.interfaces.ITankProvider;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.packet.PacketSetTankSlot;
import uk.joshiejack.penguinlib.util.handlers.QuadDirectionalTank;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileCookingFluids extends TileCooking implements ITankProvider {
    public TileCookingFluids(Appliance appliance, int timeRequired) { super(appliance, timeRequired); }
    public TileCookingFluids(Appliance appliance) {
        super(appliance);
    }

    //Fluid storage
    private final QuadDirectionalTank tank = new QuadDirectionalTank();

    @Override
    public boolean handleFluids(@Nullable EntityPlayer player, EnumHand hand, ItemStack held) {
        return tryEmpty(player, held, hand, 0) || tryEmpty(player, held, hand, 1) || tryEmpty(player, held, hand, 2) || tryEmpty(player, held, hand, 3)
                || tryFill(player, held, hand, 0) || tryFill(player, held, hand, 1) || tryFill(player, held, hand, 2) || tryFill(player, held, hand, 3);
    }

    private boolean tryFill(@Nullable EntityPlayer player, ItemStack held, EnumHand hand, int slot) {
        FluidActionResult result = FluidUtil.tryFillContainer(held, tank.get(slot), 1000, player, true);
        if (result.success) {
            if (player != null) player.setHeldItem(hand, result.result);
            if (!world.isRemote) {
                PenguinNetwork.sendToNearby(this, new PacketSetTankSlot(pos, slot, tank.getFluid(slot)));
            }

            return true;
        }

        return false;
    }

    private boolean tryEmpty(@Nullable EntityPlayer player, ItemStack held, EnumHand hand, int slot) {
        FluidActionResult result = FluidUtil.tryEmptyContainer(held, tank.get(slot), 1000, player, true);
        if (result.success) {
            if (player != null) player.setHeldItem(hand, result.result);
            if (!world.isRemote) {
                PenguinNetwork.sendToNearby(this, new PacketSetTankSlot(pos, slot, tank.getFluid(slot)));
            }

            return true;
        }

        return false;
    }

    @Override
    public List<FluidStack> getFluids() {
        return tank.getFluids();
    }

    @Override
    public void setTank(int slot, FluidStack stack) {
        tank.set(slot, stack);
    }

    @Override
    public void onFinished() {
        for (int i = 0; i < 4; i++) {
            tank.set(i, null);
            PenguinNetwork.sendToNearby(this, new PacketSetTankSlot(pos, i, null));
        }
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && tank.hasTank(facing)) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked, ConstantConditions")
    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank.getHandlerFromFacing(facing);
        else return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        tank.deserializeNBT(nbt.getCompoundTag("Fluids"));
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Fluids", tank.serializeNBT());
        return super.writeToNBT(nbt);
    }
}
