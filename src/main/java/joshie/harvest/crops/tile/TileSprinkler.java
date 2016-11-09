package joshie.harvest.crops.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.base.tile.TileDaily;
import joshie.harvest.core.helpers.MCServerHelper;
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

import static joshie.harvest.crops.HFCrops.SPRINKLER_DRAIN_RATE;

public class TileSprinkler extends TileDaily implements ITickable {
    protected double height;
    protected int range;
    protected int tick;

    public TileSprinkler() {
        height = 0.7D;
        range = 4;
    }

    protected double getRandomDouble() {
        return worldObj.rand.nextDouble() - 0.5D;
    }

    @Override
    public void update() {
        if (worldObj.isRemote) {
            if (tick % 15 == 0 && (SPRINKLER_DRAIN_RATE <= 0 || tank.getFluidAmount() > 1) && CalendarHelper.isBetween(worldObj, 6000, 6250) && !worldObj.isRaining()) {
                int setting = (2 - Minecraft.getMinecraft().gameSettings.particleSetting);
                for (int i = 0; i < setting * 32; i++) {
                    double one = getRandomDouble();
                    double two = getRandomDouble();

                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one, 0D, two);
                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one - 0.05D, 0D, two - 0.05D);
                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one - 0.05D, 0D, two + 0.05D);
                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one + 0.05D, 0D, two - 0.05D);
                    worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one + 0.05D, 0D, two + 0.05D);
                }
            }

            tick++;
        }
    }

    public boolean hydrateSoil() {
        boolean ret = false;
        for (int x = -range; x <= range; x++) {
            for (int z = -range; z <= range; z++) {
                for (int y = 0; y >= -1; y--) {
                    BlockPos position = new BlockPos(getPos().getX() + x, getPos().getY() + y, getPos().getZ() + z);
                    if (!position.equals(getPos())) {
                        if(HFApi.crops.hydrateSoil(null, getWorld(), position) && !ret) {
                            ret = true;
                        }
                    }
                }
            }
        }

        return ret;
    }

    @Override
    public void newDay() {
        if (SPRINKLER_DRAIN_RATE <= 0 || tank.getFluidAmount() > 1) {
            //Reduce the amount in the tank
            if (hydrateSoil() && SPRINKLER_DRAIN_RATE > 0) {
                tank.drainInternal(SPRINKLER_DRAIN_RATE, true);
                if (tank.getFluidAmount() <= 1) {
                    MCServerHelper.markTileForUpdate(this);
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
    protected final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME) {
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
