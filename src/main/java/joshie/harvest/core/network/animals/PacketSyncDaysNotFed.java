package joshie.harvest.core.network.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public class PacketSyncDaysNotFed extends AbstractSyncByte implements IMessageHandler<PacketSyncDaysNotFed, IMessage> {
    public PacketSyncDaysNotFed(){}
    public PacketSyncDaysNotFed(int id, byte data) {
        super(id, data);
    }

    @Override
    public IMessage onMessage(PacketSyncDaysNotFed message, MessageContext ctx) {
        IAnimalTracked entity = getAnimal(message, ctx);
        if (entity != null) {
            entity.getData().setDaysNotFed(getByte(message));
        }

        return null;
    }
}