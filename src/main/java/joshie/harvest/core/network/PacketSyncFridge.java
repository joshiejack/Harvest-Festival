package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.penguin.PenguinPacket;
import joshie.harvest.player.fridge.FridgeData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@Packet(isSided = true, side = Side.CLIENT)
public class PacketSyncFridge extends PenguinPacket {
    private NBTTagCompound nbt;

    public PacketSyncFridge() {
    }

    public PacketSyncFridge(FridgeData fridge) {
        nbt = new NBTTagCompound();
        fridge.writeToNBT(nbt);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, nbt);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        nbt = ByteBufUtils.readTag(buf);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        FridgeData fridge = new FridgeData();
        fridge.readFromNBT(nbt);
        HFTrackers.getClientPlayerTracker().setFridge(fridge);
    }
}