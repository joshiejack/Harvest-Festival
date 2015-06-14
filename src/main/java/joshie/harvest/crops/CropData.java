package joshie.harvest.crops;

import static joshie.harvest.core.network.PacketHandler.sendToEveryone;
import io.netty.buffer.ByteBuf;

import java.util.Random;
import java.util.UUID;

import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.core.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.api.crops.ICropRenderHandler.PlantSection;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.network.PacketSyncCrop;
import joshie.harvest.init.HFBlocks;
import joshie.harvest.init.HFConfig;
import joshie.harvest.init.HFCrops;
import joshie.harvest.plugins.HFPlugins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.ByteBufUtils;

public class CropData implements ICropData {
    private static final Random rand = new Random();

    private boolean isReal; //Is true if there actually is a plant here, rather than a placeholder
    private UUID owner; //The owners uuid
    private ICrop crop; //The Crop Type of this plant
    private int stage; //The stage it is currently at
    private int daysWithoutWater; //The number of days this crop has gone without water
    private WorldLocation location;

    public CropData() {}

    public CropData(WorldLocation location) {
        this.location = location;
        this.crop = (Crop) HFCrops.null_crop;
    }

    public CropData(EntityPlayer owner, Crop crop, WorldLocation location) {
        this.crop = crop;
        this.stage = 1;
        if (owner != null) {
            this.owner = UUIDHelper.getPlayerUUID(owner);
        }

        this.isReal = true;
        this.location = location;
    }
    
    @Override
    public ICropData setCrop(EntityPlayer owner, ICrop crop, int stage) {
        this.crop = crop;
        
        if (crop.isDouble(this.stage) && !crop.isDouble(stage)) {
            DimensionManager.getWorld(location.dimension).setBlockToAir(location.x, location.y + 1, location.z);
        }
        
        this.stage = stage;
        this.isReal = true;
        if (owner != null) {
            this.owner = UUIDHelper.getPlayerUUID(owner);
        }
        
        return this;
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

    //Returns false if the crop was withered
    public boolean newDay() {
        //Stage 1, Check how long the plant has been without water, If it's more than 2 days kill it
        if ((crop.requiresWater() && daysWithoutWater > 2) || isWrongSeason()) {
            return false;
        } else { //Stage 2: Now that we know, it has been watered, Update it's stage
            //If we aren't ticking randomly, Then increase the stage
            if (!Crops.ALWAYS_GROW) {
                grow();
            }
        }

        //Stage 6, Reset the water counter and fertilising
        daysWithoutWater++;

        //If AgriCraft is loaded, set the blocks metadata accordingly
        if(HFPlugins.AGRICRAFT_LOADED) {
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
            DimensionManager.getWorld(location.dimension).setBlock(location.x, location.y + 1, location.z, HFBlocks.crops, BlockCrop.FRESH_DOUBLE, 2);
        }
        
        //If the crop grows a block to the side
        if (crop.growsToSide() != null) {
            if (stage == crop.getStages()) {
                if (!attemptToGrowToSide()) {
                    stage--; //If we failed to grow, decrease the growth stage
                }
            }
        }
    }
    
    private boolean attemptToGrowToSide() {
        World world = DimensionManager.getWorld(location.dimension);
        int x = location.x;
        int y = location.y;
        int z = location.z;
        
        if (world.getBlock(x + 1, y, z).isAir(world, x + 1, y, z)) { //If it's air, then let's grow some shit
            return world.setBlock(x + 1, y, z, crop.growsToSide(), 0, 2); //0 = x-
        } else if (world.getBlock(x, y, z - 1).isAir(world, x, y, z - 1)) {
            return world.setBlock(x, y, z - 1, crop.growsToSide(), 1, 2); //1 = z+
        } else if (world.getBlock(x, y, z + 1).isAir(world, x, y, z + 1)) {
            return world.setBlock(x, y, z + 1, crop.growsToSide(), 2, 2); //2 = z-
        } else if (world.getBlock(x - 1, y, z).isAir(world, x - 1, y, z)) {
            return world.setBlock(x - 1, y, z, crop.growsToSide(), 2, 2); //3 = x-
        }
        
        return false;
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
        return crop != null? crop: HFCrops.null_crop;
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
            if (doHarvest) {
                if (crop.getRegrowStage() > 0) {
                    stage = crop.getRegrowStage();
                }
            }

            return crop.getCropStack();
        } else return null;
    }

    public void setHydrated() {
        daysWithoutWater = 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        isReal = nbt.getBoolean("IsReal");
        crop = HFConfig.mappings.getCrop(nbt.getString("CropUnlocalized"));
        stage = nbt.getByte("CurrentStage");
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
        }

        buf.writeBoolean(false);
    }

    public void fromBytes(ByteBuf buf) {
        if (buf.readBoolean()) {
            isReal = buf.readBoolean();
            crop = HFConfig.mappings.getCrop(ByteBufUtils.readUTF8String(buf));
            stage = buf.readByte();
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