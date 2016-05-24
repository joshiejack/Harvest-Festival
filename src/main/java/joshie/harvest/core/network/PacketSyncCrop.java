package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.crops.ICropData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.crops.CropData;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketSyncCrop extends AbstractPacketLocation {
    private boolean isRemoval;
    private ICropData data;

    public PacketSyncCrop() { }
    public PacketSyncCrop (int dimension, BlockPos pos, ICropData data) {
        super(dimension, pos);
        this.isRemoval = false;
        this.data = data;
    }

    public PacketSyncCrop(int dimension, BlockPos pos) {
        super(dimension, pos);
        this.isRemoval = true;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(isRemoval);
        if (!isRemoval) {
            NBTTagCompound tag = new NBTTagCompound();
            data.writeToNBT(tag);
            ByteBufUtils.writeTag(buf, tag);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        isRemoval = buf.readBoolean();
        if (!isRemoval) {
            data = new CropData(pos);
            NBTTagCompound tag = ByteBufUtils.readTag(buf);
            data.readFromNBT(tag);
        }
    }

    @Override
    public void handleQueuedClient(NetHandlerPlayClient handler) {
        if (MCClientHelper.getWorld().provider.getDimension() == dim) {
            handlePacket(MCClientHelper.getPlayer());
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (player != null) { //TODO: Check this is working, as i removed the dimension parameter, so it may not work correctly in dimensions
            HFTrackers.getCropTracker(player.worldObj).updateClient(pos, data, isRemoval);
            MCClientHelper.refresh(dim, pos);
        }
    }
}