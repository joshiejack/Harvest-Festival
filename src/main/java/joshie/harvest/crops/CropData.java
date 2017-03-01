package joshie.harvest.crops;

import joshie.harvest.api.crops.Crop;
import joshie.harvest.api.crops.IStateHandler.PlantSection;
import joshie.harvest.crops.block.BlockHFCrops.CropType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class CropData {
    private static final Random rand = new Random();
    private Crop crop = Crop.NULL_CROP; //The Crop Type of this plant
    private int stage = 1; //The stage it is currently at
    private int daysWithoutWater; //The number of days this crop has gone without water
    private boolean safe;
    private boolean isDead;

    @SuppressWarnings("WeakerAccess")
    public CropData setCrop(Crop crop, int stage) {
        this.crop = crop;
        this.stage = stage;
        this.daysWithoutWater = 1;
        return this;
    }

    //Returns false if the crop was withered
    @SuppressWarnings("unchecked")
    public boolean newDay(World world, BlockPos pos) {
        //Stage 1, Check how long the plant has been without water, If it's more than 2 days kill it
        if (isDead || crop == null || (crop.requiresWater() && daysWithoutWater > 2) || !crop.getGrowthHandler().canGrow(world, pos, crop)) {
            isDead = true;
            return false;
        } else { //Stage 2: Now that we know, it has been watered, Update it's stage
            //If we aren't ticking randomly, Then increase the stage
            if (HFCrops.GROWS_DAILY) {
                if (daysWithoutWater == 0 || !crop.requiresWater()) {
                    grow(world, pos);
                }
            }
        }

        //Stage 6, Reset the water counter and fertilising
        daysWithoutWater++;
        return true;
    }

    @SuppressWarnings("unchecked")
    public void grow(World world, BlockPos pos) {
        stage = getCrop().getGrowthHandler().grow(world, pos, crop, stage);
        if (stage == 0) {
            world.setBlockToAir(pos);
        }

        //If the crop has become double add in the new block
        if (crop.isTurningToDouble(stage)) {
            world.setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(CropType.FRESH_DOUBLE), 2);
        }
    }

    public ResourceLocation getResource() {
        return crop.getResource();
    }

    public int getStage() {
        return stage;
    }

    @Nonnull
    public Crop getCrop() {
        return crop;
    }

    public boolean isClearable() {
        if (safe) {
            safe = false;
            return false;
        } else return true;
    }

    public boolean markSafe(World world, BlockPos pos, PlantSection section) {
        this.safe = true;
        if (section == PlantSection.BOTTOM) return world.setBlockToAir(pos.up());
        else return world.setBlockToAir(pos);
    }

    @SuppressWarnings("unchecked, unused")
    public List<ItemStack> harvest(@Nullable EntityPlayer player, boolean doHarvest) {
        if (crop != null && crop.getGrowthHandler().canHarvest(crop, stage)) {
            if (crop.getGrowthHandler().canHarvest(crop, stage)) {
                int originalStage = stage;
                if (doHarvest) {
                    if (crop.getRegrowStage() > 0) {
                        stage = crop.getRegrowStage();
                    }
                }

                return crop.getDropHandler().getDrops(crop, originalStage, rand);
            } else return null;
        } else {
            return null;
        }
    }

    public boolean isWatered() {
        return daysWithoutWater == 0;
    }

    public void setHydrated() {
        daysWithoutWater = 0;
    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("CropResource")) {
            isDead = nbt.getBoolean("IsDead");
            crop = Crop.REGISTRY.get(new ResourceLocation(nbt.getString("CropResource")));
            stage = nbt.getByte("CurrentStage");
            daysWithoutWater = nbt.getShort("DaysWithoutWater");
        }

        if (crop == null) crop = Crop.NULL_CROP;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        if (crop != null) {
            nbt.setBoolean("IsDead", isDead);
            nbt.setString("CropResource", crop.getResource().toString());
            nbt.setByte("CurrentStage", (byte) stage);
            nbt.setShort("DaysWithoutWater", (short) daysWithoutWater);
        }

        return nbt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        CropData other = (CropData) obj;
        if (crop == null) {
            if (other.crop != null) return false;
        } else if (!crop.equals(other.crop)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((crop == null) ? 0 : crop.hashCode());
        return result;
    }
}