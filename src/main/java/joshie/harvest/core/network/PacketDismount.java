package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketDismount implements IMessage, IMessageHandler<PacketDismount, IMessage> {
    public PacketDismount() {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(PacketDismount message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player.riddenByEntity instanceof EntityChicken) {
            EntityChicken chicken = (EntityChicken) player.riddenByEntity;
            IAnimalTracked tracked = (IAnimalTracked) chicken;
            chicken.mountEntity(null);
            chicken.rotationPitch = player.rotationPitch;
            chicken.rotationYaw = player.rotationYaw;
            chicken.moveFlying(0F, 1.0F, 1.25F);
            tracked.getData().dismount(player);
        }

        return null;
    }
}