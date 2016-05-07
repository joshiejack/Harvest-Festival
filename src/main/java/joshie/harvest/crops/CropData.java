package joshie.harvest.crops;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.HFApi;
import joshie.harvest.api.WorldLocation;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.ICrop;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.blocks.BlockCrop;
import joshie.harvest.blocks.HFBlocks;
import joshie.harvest.core.config.Crops;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.UUIDHelper;
import joshie.harvest.core.network.PacketSyncCrop;
import joshie.harvest.plugins.HFPlugins;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.Random;
import java.util.UUID;

import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

public class CropData implements ICropData {
    private static final Random rand = new Random();

    private boolean isReal; //Is true if there actually is a plant here, rather than a placeholder
    private UUID owner; //The owners uuid
    private ICrop crop; //The Crop Type of this plant
    private int stage; //The stage it is currently at
    private int daysWithoutWater; //The number of days this crop has gone without water
    private WorldLocation location;

    public CropData() {
    }

    public CropData(WorldLocation location) {
        this.location = location;
        this.crop = HFCrops.null_crop;
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
            DimensionManager.getWorld(location.dimension).setBlockToAir(location.position.up());
        }

        this.stage = stage;
        this.isReal = true;
        if (owner != null) {
            this.owner = UUIDHelper.getPlayerUUID(owner);
        }

        return this;
    }

    private boolean isWrongSeason() {
        Season toMatch = HFTrackers.getCalendar().getDate().getSeason();
        for (Season season : crop.getSeasons()) {
            if (toMatch == season) return false;
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
        if (HFPlugins.AGRICRAFT_LOADED) {
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
        World world = DimensionManager.getWorld(location.dimension);
        world.setBlockState(location.position, world.getBlockState(location.position).getBlock().getStateFromMeta(meta), 2);
    }

    //Called when the crop that was on this farmland is destroyed
    @Override
    public void clear() {
        this.isReal = false;
        sendToEveryone(new PacketSyncCrop(location, this));
    }

    @Override
    public void grow() {
        if (daysWithoutWater == 0 || !crop.requiresWater()) {
            //Increase the stage of this crop
            if (stage < crop.getStages()) {
                stage++;
            }

            //If the crop has become double add in the new block
            if (crop.isDouble(stage)) {
                DimensionManager.getWorld(location.dimension).setBlockState(location.position.up(), HFBlocks.CROPS.getStateFromEnum(BlockCrop.Stage.FRESH_DOUBLE), 2);
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
    }

    private boolean attemptToGrowToSide() {
        World world = DimensionManager.getWorld(location.dimension);
        BlockPos pos = location.position;

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
    public WorldLocation getLocation() {
        return location;
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
        isReal = nbt.getBoolean("IsReal");
        crop = HFApi.CROPS.getCrop(new ResourceLocation(nbt.getString("CropResource")));
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
            nbt.setString("CropResource", crop.getResource().toString());
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
            ByteBufUtils.writeUTF8String(buf, crop.getResource().toString());
            buf.writeByte(stage);
        }

        buf.writeBoolean(false);
    }

    public void fromBytes(ByteBuf buf) {
        if (buf.readBoolean()) {
            isReal = buf.readBoolean();
            crop = HFApi.CROPS.getCrop(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
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