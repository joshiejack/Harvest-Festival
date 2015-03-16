package joshie.harvestmoon.crops;

import static joshie.harvestmoon.core.network.PacketHandler.sendToEveryone;
import io.netty.buffer.ByteBuf;

import java.util.Random;
import java.util.UUID;

import joshie.harvestmoon.api.WorldLocation;
import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.api.crops.ICropRenderHandler.PlantSection;
import joshie.harvestmoon.blocks.BlockCrop;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.helpers.SeasonHelper;
import joshie.harvestmoon.core.helpers.UUIDHelper;
import joshie.harvestmoon.core.network.PacketSyncCrop;
import joshie.harvestmoon.init.HMBlocks;
import joshie.harvestmoon.init.HMConfiguration;
import joshie.harvestmoon.init.HMCrops;
import joshie.harvestmoon.plugins.HMPlugins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.ByteBufUtils;

public class CropData implements ICropData {
    private static final Random rand = new Random();

    private boolean isReal; //Is true if there actually is a plant here, rather than a placeholder
    private UUID owner; //The owners uuid
    private ICrop crop; //The Crop Type of this plant
    private int stage; //The stage it is currently at
    private int quality; //The quality of this crop
    private boolean isFertilized; //Whether this crop is currently fertilized
    private int daysWithoutWater; //The number of days this crop has gone without water
    private WorldLocation location;

    public CropData() {}

    public CropData(WorldLocation location) {
        this.location = location;
        this.crop = (Crop) HMCrops.null_crop;
    }

    public CropData(EntityPlayer owner, Crop crop, int quality, WorldLocation location) {
        this.crop = crop;
        this.quality = quality;
        this.stage = 1;
        if (owner != null) {
            this.owner = UUIDHelper.getPlayerUUID(owner);
        }

        this.isReal = true;
        this.location = location;
    }
    
    @Override
    public ICropData setCrop(EntityPlayer owner, ICrop crop, int quality, int stage) {
        this.crop = crop;
        this.quality = quality;
        this.stage = stage;
        this.isReal = true;
        if (owner != null) {
            this.owner = UUIDHelper.getPlayerUUID(owner);
        }
        
        return this;
    }

    private boolean isWrongSeason() {
        for (Season season : SeasonHelper.getSeasonsFromISeasons(crop.getSeasons())) {
            if (CalendarHelper.getSeason() == season) return false;
        }

        return true;
    }

    public boolean isReal() {
        return isReal;
    }

    //Returns false if the crop was withered
    public boolean newDay() {
        //Stage 1, Check how long the plant has been without water, If it's more than 2 days kill it, decrease it's quality if it's not been watered as well
        if ((crop.requiresWater() && daysWithoutWater > 2) || isWrongSeason()) {
            return false;
        } else { //Stage 2: Now that we know, it has been watered, Update it's stage
            //If we aren't ticking randomly, Then increase the stage and quality
            if (!Crops.ALWAYS_GROW) {
                grow();
            }

            //If it's fertilized increase it's quality
            if (isFertilized && quality < 100) {
                quality++;
            }
        }

        //Stage 6, Reset the water counter and fertilising
        daysWithoutWater++;
        isFertilized = false;

        //If AgriCraft is loaded, set the blocks metadata accordingly
        if(HMPlugins.AGRICRAFT_LOADED) {
            //Stage 7, Set the metadata to 7 if fully grown
            if (stage >= crop.getStages()) {
                //Set the Crop Metadata to 7, when it has fully grown
                setCropMetaData(location, 7);
            } else {
                setCropMetaData(location, 0);
            }
        }

        return true;
    }

    public void setCropMetaData(WorldLocation location, int meta) {
        DimensionManager.getWorld(location.dimension).setBlockMetadataWithNotify(location.x, location.y, location.z, meta, 2);
    }

    //Called when the crop that was on this farmland is destroyed
    @Override
    public void clear() {
        this.isReal = false;        
        sendToEveryone(new PacketSyncCrop(location, this));
    }

    @Override
    public void grow() {
        //Increase the stage of this crop
        if (stage < crop.getStages()) {
            stage++;
        }
        
        //If the crop has become double add in the new block
        if (crop.isDouble(stage)) {
            DimensionManager.getWorld(location.dimension).setBlock(location.x, location.y + 1, location.z, HMBlocks.crops, BlockCrop.FRESH_DOUBLE, 2);
        }
    }

    public String getName() {
        return crop.getUnlocalizedName();
    }

    @Override
    public WorldLocation getLocation() {
        return location;
    }

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public ICrop getCrop() {
        return crop != null? crop: HMCrops.null_crop;
    }

    @Override
    public int getQuality() {
        return quality;
    }

    @Override
    public IIcon getCropIcon(PlantSection section) {
        return getCrop().getCropRenderHandler().getIconForStage(section, getStage());
    }

    public boolean canGrow() {
        //TODO: Fix PlayHelperifOn...
        //(PlayerHelper.isOnlineOrFriendsAre(owner));
        return isReal;
    }
    
    public ItemStack harvest(EntityPlayer player, boolean doHarvest) {
        if (crop == null) return null;
        if (stage >= crop.getStages()) {
            int cropQuality = 0;
            if (!crop.isStatic()) {
                cropQuality = ((this.quality - 1) * 100);
            }

            if (doHarvest) {
                if (crop.getRegrowStage() > 0) {
                    stage = crop.getRegrowStage();
                }
            }

            return crop.getCropStackForQuality(cropQuality);
        } else return null;
    }

    public void setHydrated() {
        daysWithoutWater = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        isReal = nbt.getBoolean("IsReal");
        crop = HMConfiguration.mappings.getCrop(nbt.getString("CropUnlocalized"));
        stage = nbt.getByte("CurrentStage");
        quality = nbt.getByte("CropQuality");
        isFertilized = nbt.getBoolean("IsFertilized");
        daysWithoutWater = nbt.getShort("DaysWithoutWater");

        if (nbt.hasKey("Farmer-UUIDMost")) {
            owner = new UUID(nbt.getLong("Farmer-UUIDMost"), nbt.getLong("Farmer-UUIDLeast"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        if (crop != null) {
            nbt.setBoolean("IsReal", isReal);
            nbt.setString("CropUnlocalized", crop.getUnlocalizedName());
            nbt.setByte("CurrentStage", (byte) stage);
            nbt.setByte("CropQuality", (byte) quality);
            nbt.setBoolean("IsFertilized", isFertilized);
            nbt.setShort("DaysWithoutWater", (short) daysWithoutWater);

            if (owner != null) {
                nbt.setLong("Farmer-UUIDMost", owner.getMostSignificantBits());
                nbt.setLong("Farmer-UUIDLeast", owner.getLeastSignificantBits());
            }
        }
    }

    /* Packet Based */
    public void toBytes(ByteBuf buf) {
        if (crop != null) {
            buf.writeBoolean(true);
            buf.writeBoolean(isReal);
            ByteBufUtils.writeUTF8String(buf, crop.getUnlocalizedName());
            buf.writeByte(stage);
            buf.writeByte(quality);
            buf.writeBoolean(isFertilized);
        }

        buf.writeBoolean(false);
    }

    public void fromBytes(ByteBuf buf) {
        if (buf.readBoolean()) {
            isReal = buf.readBoolean();
            crop = HMConfiguration.mappings.getCrop(ByteBufUtils.readUTF8String(buf));
            stage = buf.readByte();
            quality = buf.readByte();
            isFertilized = buf.readBoolean();
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
        if (quality != other.quality) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((crop == null) ? 0 : crop.hashCode());
        result = prime * result + quality;
        return result;
    }
}