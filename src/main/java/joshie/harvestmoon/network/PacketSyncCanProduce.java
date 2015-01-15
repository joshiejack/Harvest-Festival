package joshie.harvestmoon.network;

import static joshie.harvestmoon.HarvestMoon.handler;
import static joshie.harvestmoon.helpers.AnimalHelper.canProduceProduct;
import static joshie.harvestmoon.network.PacketHandler.sendToClient;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncCanProduce implements IMessage, IMessageHandler<PacketSyncCanProduce, IMessage> {
    private boolean isSenderClient;
    private boolean canProduce;
    private int id;

    public PacketSyncCanProduce() {}

    public PacketSyncCanProduce(int id, boolean isSenderClient) {
        this.isSenderClient = isSenderClient;
        this.id = id;
    }

    public PacketSyncCanProduce(int id, boolean isSenderClient, boolean canProduce) {
        this(id, isSenderClient);
        this.canProduce = canProduce;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isSenderClient);
        buf.writeInt(id);
        if (!isSenderClient) {
            buf.writeBoolean(canProduce);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isSenderClient = buf.readBoolean();
        id = buf.readInt();
        if (!isSenderClient) {
            canProduce = buf.readBoolean();
        }
    }

    @Override
    public IMessage onMessage(PacketSyncCanProduce message, MessageContext ctx) {
        if (message.isSenderClient) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            Entity entity = player.worldObj.getEntityByID(message.id);
            if (entity instanceof EntityAnimal) {
                sendToClient(new PacketSyncCanProduce(message.id, false, canProduceProduct((EntityAnimal) entity)), player);
            }
        } else {
            Entity entity = joshie.lib.helpers.ClientHelper.getWorld().getEntityByID(message.id);
            if (entity instanceof EntityAnimal) {
                handler.getClient().getAnimalTracker().setCanProduceProduct((EntityAnimal) entity, message.canProduce);
            }
        }

        return null;
    }
}