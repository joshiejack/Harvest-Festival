package joshie.harvest.core.network.animals;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractSyncAnimal implements IMessage {
    private int id;

    public AbstractSyncAnimal() {}
    public AbstractSyncAnimal(int id) {
        this.id = id;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
    }
    
    public IAnimalTracked getAnimal(AbstractSyncAnimal message, MessageContext ctx) {
        return (IAnimalTracked) MCClientHelper.getWorld().getEntityByID(message.id);
    }
}