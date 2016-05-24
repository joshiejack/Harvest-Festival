package joshie.harvest.crops;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.crops.blocks.BlockHFCrops;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CropData implements ICropData {
    private boolean isReal; //Is true if there actually is a plant here, rather than a placeholder
    private ICrop crop; //The Crop Type of this plant
    private int stage; //The stage it is currently at
    private int daysWithoutWater; //The number of days this crop has gone without water
    private BlockPos pos;

    public CropData(BlockPos pos) {
        this.pos = pos;
        this.crop = HFCrops.null_crop;
    }

    @Override
    public ICropData setCrop(EntityPlayer owner, ICrop crop, int stage) {
        this.crop = crop;

        if (crop.isDouble(this.stage) && !crop.isDouble(stage)) {
            owner.worldObj.setBlockToAir(pos.up());
        }

        this.stage = stage;
        this.isReal = true;
        return this;
    }

    private boolean isWrongSeason(World world) {
        Season toMatch = HFTrackers.getCalendar(world).getSeasonAt(pos);
        for (Season season : crop.getSeasons()) {
            if (toMatch == season) return false;
        }

        return true;
    }

    //Returns false if the crop was withered
    public boolean newDay(World world) {
        //Stage 1, Check how long the plant has been without water, If it's more than 2 days kill it
        if ((crop.requiresWater() && daysWithoutWater > 2) || isWrongSeason(world)) {
            return false;
        } else { //Stage 2: Now that we know, it has been watered, Update it's stage
            //If we aren't ticking randomly, Then increase the stage
            if (!Crops.alwaysGrow) {
                grow(world);
            }
        }

        //Stage 6, Reset the water counter and fertilising
        daysWithoutWater++;
        return true;
    }

    @Override
    public void grow(World world) {
        if (daysWithoutWater == 0 || !crop.requiresWater()) {
            //Increase the stage of this crop
            if (stage < crop.getStages()) {
                stage++;
            }

            //If the crop has become double add in the new block
            if (crop.isDouble(stage)) {
                world.setBlockState(pos.up(), HFBlocks.CROPS.getStateFromEnum(BlockHFCrops.Stage.FRESH_DOUBLE), 2);
            }

            //If the crop grows a block to the side
            if (crop.growsToSide() != null) {
                if (stage == crop.getStages()) {
                    if (!attemptToGrowToSide(world)) {
                        stage--; //If we failed to grow, decrease the growth stage
                    }
                }
            }
        }
    }

    private boolean attemptToGrowToSide(World world) {
        if (world.isAirBlock(pos.add(1, 0, 0))) { //If it's air, then let's grow some shit
            return world.setBlockState(pos.add(1, 0, 0), crop.growsToSide().getStateFromMeta(0), 2); //0 = x-
        } else if (world.isAirBlock(pos.add(0, 0, -1))) {
            return world.setBlockState(pos.add(0, 0, -1), crop.growsToSide().getStateFromMeta(1), 2); //1 = z+
        } else if (world.isAirBlock(pos.add(0, 0, 1))) {
            return world.setBlockState(pos.add(0, 0, 1), crop.growsToSide().getStateFromMeta(2), 2); //2 = z-
        } else if (world.isAirBlock(pos.add(-1, 0, 0))) {
            return world.setBlockState(pos.add(-1, 0, 0), crop.growsToSide().getStateFromMeta(2), 2); //3 = x-
        }
        return false;
    }

    public ResourceLocation getResource() {
        return crop.getResource();
    }

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public ICrop getCrop() {
        return crop != null ? crop : HFCrops.null_crop;
    }

    public boolean canGrow() {
        //TODO: Fix PlayHelperifOn...
        //(PlayerHelper.isOnlineOrFriendsAre(owner));
        return isReal;
    }

    public ItemStack harvest(EntityPlayer player, boolean doHarvest) {
        if (crop == null) return null;
        if (stage >= crop.getStages()) {
            if (doHarvest) {
                if (crop.getRegrowStage() > 0) {
                    stage = crop.getRegrowStage();
                }
            }

            return crop.getHarvested();
        } else return null;
    }

    public void setHydrated() {
        daysWithoutWater = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        crop = HFApi.crops.getCrop(new ResourceLocation(nbt.getString("CropResource")));
        isReal = nbt.getBoolean("IsReal");
        if (crop == HFCrops.null_crop) isReal = false;
        stage = nbt.getByte("CurrentStage");
        daysWithoutWater = nbt.getShort("DaysWithoutWater");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        if (crop != null) {
            nbt.setBoolean("IsReal", isReal);
            nbt.setString("CropResource", crop.getResource().toString());
            nbt.setByte("CurrentStage", (byte) stage);
            nbt.setShort("DaysWithoutWater", (short) daysWithoutWater);
        }
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