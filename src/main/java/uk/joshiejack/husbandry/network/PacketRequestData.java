package uk.joshiejack.husbandry.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.SERVER)
public class PacketRequestData extends PenguinPacket {
    private int entityID;

    public PacketRequestData(){}
    public PacketRequestData(int entityID) {
        this.entityID = entityID;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(entityID);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        entityID = from.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(entityID);
        if (entity != null) {
            AnimalStats<?> stats = AnimalStats.getStats(entity);
            if (stats != null) {
                PenguinNetwork.sendToClient(new PacketSendData(entityID, stats), player);
            }
        }
    }
}
