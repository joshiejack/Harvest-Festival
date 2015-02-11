package joshie.harvestmoon.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.helpers.generic.ClientHelper;
import joshie.harvestmoon.init.mode.EasyMode;
import joshie.harvestmoon.init.mode.HardMode;
import joshie.harvestmoon.util.Translate;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSetDifficulty implements IMessage, IMessageHandler<PacketSetDifficulty, IMessage> {
    private boolean senderIsClient;
    private boolean easy;

    public PacketSetDifficulty() {}

    public PacketSetDifficulty(boolean easy, boolean senderIsClient) {
        this.senderIsClient = senderIsClient;
        this.easy = easy;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(senderIsClient);
        buf.writeBoolean(easy);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        senderIsClient = buf.readBoolean();
        easy = buf.readBoolean();
    }

    @Override
    public IMessage onMessage(PacketSetDifficulty message, MessageContext ctx) {
        boolean isClient = message.senderIsClient;
        boolean easy = message.easy;

        if (easy) EasyMode.init();
        else HardMode.init();
        
        if (isClient) {
            PacketHandler.sendToEveryone(new PacketSetDifficulty(easy, false));
        } else ClientHelper.addToChat(Translate.translate("mode." + easy));

        return null;
    }
}