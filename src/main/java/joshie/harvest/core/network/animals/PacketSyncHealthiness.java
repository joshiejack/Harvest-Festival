package joshie.harvest.core.network.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncHealthiness extends AbstractSyncByte implements IMessageHandler<PacketSyncHealthiness, IMessage> {
    public PacketSyncHealthiness(){}
    public PacketSyncHealthiness(int id, byte data) {
        super(id, data);
    }

    @Override
    public IMessage onMessage(PacketSyncHealthiness message, MessageContext ctx) {
        IAnimalTracked entity = getAnimal(message, ctx);
        if (entity != null) {
            entity.getData().setHealthiness(getByte(message));
        }

        return null;
    }
}