package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.commands.CommandManager;
import joshie.harvest.core.commands.HFCommandBase;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketCommand implements IMessage, IMessageHandler<PacketCommand, IMessage> {
    private HFCommandBase command;
    private String[] data;

    public PacketCommand() {}
    public PacketCommand(HFCommandBase command, String... data) {
        this.command = command;
        this.data = data;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, command.getCommandName());
        buf.writeInt(data.length);
        for (int i = 0; i < data.length; i++) {
            ByteBufUtils.writeUTF8String(buf, data[i]);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        command = CommandManager.INSTANCE.getCommandFromString(ByteBufUtils.readUTF8String(buf));
        int amount = buf.readInt();
        data = new String[amount];
        for (int i = 0; i < amount; i++) {
            data[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public IMessage onMessage(PacketCommand message, MessageContext ctx) {
        message.command.execute(message.data);
        return null;
    }
}