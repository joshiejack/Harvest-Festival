package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.fridge.FridgeData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncFridge implements IMessage, IMessageHandler<PacketSyncFridge, IMessage> {
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
    public IMessage onMessage(PacketSyncFridge message, MessageContext ctx) {
        FridgeData fridge = new FridgeData();
        fridge.readFromNBT(message.nbt);
        HFTrackers.getClientPlayerTracker().setFridge(fridge);
        return null;
    }
}