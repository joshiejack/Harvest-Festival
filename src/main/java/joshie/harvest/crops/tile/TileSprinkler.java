package joshie.harvest.crops.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.core.base.tile.TileDaily;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.crops.HFCrops;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileSprinkler extends TileDaily implements ITickable {
    private int tick;

    @Override
    public void update() {
        if (worldObj.isRemote) {
            if (tick % 15 == 0 && tank.getFluidAmount() > 1) {
                int setting = (2 - Minecraft.getMinecraft().gameSettings.particleSetting);
                for (int i = 0; i < setting * 32; i++) {
                    double one = worldObj.rand.nextDouble() - 0.5D;
                    double two = worldObj.rand.nextDouble() - 0.5D;

                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + 0.7D, getPos().getZ() + 0.5D, one, 0D, two);
                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + 0.7D, getPos().getZ() + 0.5D, one - 0.05D, 0D, two - 0.05D);
                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + 0.7D, getPos().getZ() + 0.5D, one - 0.05D, 0D, two + 0.05D);
                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + 0.7D, getPos().getZ() + 0.5D, one + 0.05D, 0D, two - 0.05D);
                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + 0.7D, getPos().getZ() + 0.5D, one + 0.05D, 0D, two + 0.05D);
                }
            }

            tick++;
        }
    }

    public void hydrateSoil() {
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                for (int y = 0; y >= -1; y--) {
                    BlockPos position = new BlockPos(getPos().getX() + x, getPos().getY() + y, getPos().getZ() + z);
                    if (!position.equals(getPos())) {
                        HFApi.crops.hydrateSoil(null, getWorld(), position);
                    }
                }
            }
        }
    }

    @Override
    public void newDay(Phase phase) {
        if (phase == Phase.PRE) {
            if (tank.getFluidAmount() > 1) {
                //Reduce the amount in the tank
                tank.drainInternal(HFCrops.SPRINKLER_DRAIN_RATE, true);
                hydrateSoil();
                if (tank.getFluidAmount() <= 1) {
                    PacketHandler.sendRefreshPacket(this);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tank.writeToNBT(tag);
        return super.writeToNBT(tag);
    }

    /** Capabilities **/
    private final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME) {
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return fluid.getFluid() == FluidRegistry.WATER;
        }
    };

    public FluidTank getTank() {
        return tank;
    }


    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;
        return super.getCapability(capability, facing);
    }
}
