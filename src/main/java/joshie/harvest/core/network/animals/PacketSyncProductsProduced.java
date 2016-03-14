package joshie.harvest.core.network.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncProductsProduced extends AbstractSyncBoolean implements IMessageHandler<PacketSyncProductsProduced, IMessage> {
    public PacketSyncProductsProduced() {}
    public PacketSyncProductsProduced(int id, boolean data) {
        super(id, data);
    }

    @Override
    public IMessage onMessage(PacketSyncProductsProduced message, MessageContext ctx) {
        IAnimalTracked entity = getAnimal(message, ctx);
        if (entity != null) {
            entity.getData().setProductsProduced(getBoolean(message));
        }

        return null;
    }
}