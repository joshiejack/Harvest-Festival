package uk.joshiejack.husbandry.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketProduced extends PenguinPacket {
    private int entityID;
    private int value;

    public PacketProduced(){}
    public PacketProduced(int entityID, int value) {
        this.entityID = entityID;
        this.value = value;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(entityID);
        to.writeInt(value);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        entityID = from.readInt();
        value = from.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(entityID);
        if (entity != null) {
            AnimalStats<?> stats = AnimalStats.getStats(entity);
            if (stats != null) {
                stats.setProduced(value);
            }
        }
    }
}
