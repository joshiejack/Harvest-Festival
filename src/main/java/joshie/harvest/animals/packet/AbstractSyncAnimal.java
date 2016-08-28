package joshie.harvest.animals.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.helpers.generic.MCClientHelper;
import joshie.harvest.core.network.PenguinPacket;

public abstract class AbstractSyncAnimal extends PenguinPacket {
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
    
    public IAnimalTracked getAnimal() {
        return (IAnimalTracked) MCClientHelper.getWorld().getEntityByID(id);
    }
}