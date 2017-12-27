package joshie.harvest.crops.tile;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.ticking.DailyTickableBlock;
import joshie.harvest.api.ticking.DailyTickableBlock.Phases;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.base.tile.TileHarvest;
import joshie.harvest.core.helpers.MCServerHelper;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;

import static joshie.harvest.crops.HFCrops.SPRINKLER_DRAIN_RATE;

public class TileSprinkler extends TileHarvest implements ITickable {
    private static final DailyTickableBlock TICKABLE = new DailyTickableBlock(Phases.POST) {
        @Override
        public boolean isStateCorrect(World world, BlockPos pos, IBlockState state) {
            return state.getBlock() == HFCrops.SPRINKLER;
        }

        @Override
        @SuppressWarnings("ConstantConditions")
        public void newDay(World world, BlockPos pos, IBlockState state) {
            TileSprinkler sprinkler = (TileSprinkler) world.getTileEntity(pos);
            if (SPRINKLER_DRAIN_RATE <= 0 || sprinkler.getTank().getFluidAmount() > 1) {
                //Reduce the amount in the tank
                if (sprinkler.hydrateSoil() && SPRINKLER_DRAIN_RATE > 0) {
                    sprinkler.getTank().drainInternal(SPRINKLER_DRAIN_RATE, true);
                    if (sprinkler.getTank().getFluidAmount() <= 1) {
                        MCServerHelper.markTileForUpdate(sprinkler);
                    }
                }
            }
        }
    };

    /** Main tile stuff **/
    private final double height;
    private final int range;

    protected int tick;

    public TileSprinkler() {
        this(0.7D, 4);
    }

    @SuppressWarnings("WeakerAccess")
    public TileSprinkler(double height, int range) {
        this.height = height;
        this.range = range;
    }

    protected double getRandomDouble() {
        return world.rand.nextDouble() - 0.5D;
    }

    @Override
    public void update() {
        if (world.isRemote) {
            if (tick % 15 == 0 && (SPRINKLER_DRAIN_RATE <= 0 || tank.getFluidAmount() > 1) && CalendarHelper.isBetween(world, 6000, 6250) && !world.isRaining()) {
                int setting = (2 - Minecraft.getMinecraft().gameSettings.particleSetting);
                for (int i = 0; i < setting * 32; i++) {
                    double one = getRandomDouble();
                    double two = getRandomDouble();

                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one, 0D, two);
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one - 0.05D, 0D, two - 0.05D);
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one - 0.05D, 0D, two + 0.05D);
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one + 0.05D, 0D, two - 0.05D);
                    world.spawnParticle(EnumParticleTypes.WATER_SPLASH, getPos().getX() + 0.5D, getPos().getY() + height, getPos().getZ() + 0.5D, one + 0.05D, 0D, two + 0.05D);
                }
            }

            tick++;
        }
    }

    private boolean hydrateSoil() {
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
    public void validate() {
        tileEntityInvalid = false;
        HFApi.tickable.addTickable(world, pos, TICKABLE);
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

    /** ================= Capabilities =================================== **/
    private final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME) {
        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return fluid != null && fluid.getFluid() == FluidRegistry.WATER;
        }
    };

    public FluidTank getTank() {
        return tank;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;
        return super.getCapability(capability, facing);
    }
}
