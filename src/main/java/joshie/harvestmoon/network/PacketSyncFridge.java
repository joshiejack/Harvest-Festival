package joshie.harvestmoon.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.helpers.ClientHelper;
import joshie.harvestmoon.helpers.generic.MCClientHelper;
import joshie.harvestmoon.player.FridgeContents;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncFridge implements IMessage, IMessageHandler<PacketSyncFridge, IMessage> {
    private NBTTagCompound nbt;
    
    public PacketSyncFridge() {}
    public PacketSyncFridge(FridgeContents fridge) {
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
        FridgeContents fridge = new FridgeContents(MCClientHelper.getWorld());
        fridge.readFromNBT(message.nbt);
        ClientHelper.getPlayerData().setFridge(fridge);
        return null;
    }
}