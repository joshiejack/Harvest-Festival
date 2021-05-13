package uk.joshiejack.husbandry.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.network.packet.PacketSyncNBTTagCompound;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSendData extends PacketSyncNBTTagCompound {
    private int entityID;

    public PacketSendData(){}
    public PacketSendData(int entityID, AnimalStats<?> stats) {
        super(stats.serializeNBT());
        this.entityID = entityID;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(entityID);
        super.toBytes(to);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        entityID = from.readInt();
        super.fromBytes(from);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(entityID);
        if (entity != null) {
            AnimalStats<?> stats = AnimalStats.getStats(entity);
            if (stats != null) {
                stats.deserializeNBT(tag);
            }
        }
    }
}
