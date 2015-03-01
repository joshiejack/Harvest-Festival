package joshie.harvestmoon.crops;

import static joshie.harvestmoon.core.helpers.CropHelper.getCropFromOrdinal;
import static joshie.harvestmoon.core.network.PacketHandler.sendToEveryone;
import static joshie.harvestmoon.crops.CropData.WitherType.NONE;
import io.netty.buffer.ByteBuf;

import java.util.Random;
import java.util.UUID;

import joshie.harvestmoon.api.crops.ICrop;
import joshie.harvestmoon.api.crops.ICropData;
import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.config.Crops;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.network.PacketSyncCrop;
import joshie.harvestmoon.core.util.IData;
import joshie.harvestmoon.plugins.agricraft.AgriCraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.DimensionManager;

public class CropData implements IData, ICropData {
    private static final Random rand = new Random();

    public static enum WitherType {
        SEED, GROWING, GROWN, NONE;
    }

    private boolean isReal; //Is true if there actually is a plant here, rather than a placeholder
    private UUID owner; //The owners uuid
    private Crop crop; //The Crop Type of this plant
    private int stage; //The stage it is currently at
    private int quality; //The quality of this crop
    private boolean isFertilized; //Whether this crop is currently fertilized
    private int daysWithoutWater; //The number of days this crop has gone without water
    private WorldLocation location;

    public CropData() {}

    public CropData(WorldLocation location) {
        this.location = location;
    }

    public CropData(EntityPlayer owner, Crop crop, int quality, WorldLocation location) {
        this.crop = crop;
        this.quality = quality;
        this.stage = 1;
        if (owner != null) {
            this.owner = owner.getPersistentID();
        }

        this.isReal = true;
        this.location = location;
    }

    private boolean isWrongSeason() {
        for (Season season : crop.getSeasons()) {
            if (CalendarHelper.getSeason() == season) return false;
        }

        return true;
    }

    public boolean isReal() {
        return isReal;
    }

    public WitherType newDay() {
        //Stage 1, Check how long the plant has been without water, If it's more than 2 days kill it, decrease it's quality if it's not been watered as well
        if (daysWithoutWater > 2 || isWrongSeason()) {
            return crop.getWitherType(stage);
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
        if(AgriCraft.IS_LOADED) {
            //Stage 7, Set the metadata to 7 if fully grown
            if (stage >= crop.getStages()) {
                //Set the Crop Metadata to 7, when it has fully grown
                setCropMetaData(location, 7);
            } else {
                setCropMetaData(location, 0);
            }
        }

        return NONE;
    }

    public void setCropMetaData(WorldLocation location, int meta) {
        DimensionManager.getWorld(location.dimension).setBlockMetadataWithNotify(location.x, location.y + 1, location.z, meta, 2);
    }

    //Called when the crop that was on this farmland is destroyed
    @Override
    public void clear() {
        this.isReal = false;        
        sendToEveryone(new PacketSyncCrop(location, this));
    }

    @Override
    public void regrow() {
        if (crop.getRegrowStage() > 0) {
            stage = crop.getRegrowStage();
        }

        sendToEveryone(new PacketSyncCrop(location, this));
    }

    public void grow() {
        //Increase the stage of this crop
        if (stage < crop.getStages()) {
            stage++;
        }
    }

    public String getName() {
        return crop.getUnlocalizedName();
    }

    @Override
    public int getStage() {
        return stage;
    }

    @Override
    public ICrop getCrop() {
        return crop;
    }

    @Override
    public int getQuality() {
        return quality;
    }

    public boolean doesRegrow() {
        return crop.getRegrowStage() > 0;
    }

    public boolean isEdible() {
        return crop.isStatic();
    }

    public IIcon getIcon() {
        return crop.getIcon(stage);
    }

    public boolean canGrow() {
        //TODO: Fix PlayHelperifOn...
        //(PlayerHelper.isOnlineOrFriendsAre(owner));
        return isReal;
    }

    public ItemStack harvest() {
        if (stage >= crop.getStages()) {
            int cropQuality = 0;
            if (!crop.isStatic()) {
                cropQuality = ((this.quality - 1) * 100);
            }

            regrow();

            return crop.getCropStackForQuality(cropQuality);
        } else return null;
    }

    public void setHydrated() {
        daysWithoutWater = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        isReal = nbt.getBoolean("IsReal");
        crop = getCropFromOrdinal(nbt.getShort("CropMeta"));
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
            nbt.setShort("CropMeta", (short) crop.getCropMeta());
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
            buf.writeShort(crop.getCropMeta());
            buf.writeByte(stage);
            buf.writeByte(quality);
            buf.writeBoolean(isFertilized);
        }

        buf.writeBoolean(false);
    }

    public void fromBytes(ByteBuf buf) {
        if (buf.readBoolean()) {
            isReal = buf.readBoolean();
            crop = getCropFromOrdinal(buf.readShort());
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