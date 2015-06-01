package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.AnimalHelper;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketDismountChicken implements IMessage, IMessageHandler<PacketDismountChicken, IMessage> {
    public PacketDismountChicken() {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(PacketDismountChicken message, MessageContext ctx) {
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        if (player.riddenByEntity instanceof EntityChicken) {
            EntityChicken chicken = (EntityChicken) player.riddenByEntity;
            chicken.mountEntity(null);
            chicken.rotationPitch = player.rotationPitch;
            chicken.rotationYaw = player.rotationYaw;
            chicken.moveFlying(0F, 1.0F, 1.25F);
            AnimalHelper.throwChicken(player, chicken);
        }

        return null;
    }
}