package joshie.harvest.core.network;

import static joshie.harvest.core.network.PacketHandler.sendToClient;
import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.helpers.generic.MCClientHelper;
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
            IAnimalTracked entity = (IAnimalTracked) player.worldObj.getEntityByID(message.id);
            sendToClient(new PacketSyncCanProduce(message.id, false, HarvestFestival.proxy.getAnimalTracker().canProduceProduct(entity.getData())), player);
        } else {
            IAnimalTracked entity = (IAnimalTracked) MCClientHelper.getWorld().getEntityByID(message.id);
            HarvestFestival.proxy.getAnimalTracker().setCanProduceProduct(entity.getData(), message.canProduce);
        }

        return null;
    }
}