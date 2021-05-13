package uk.joshiejack.husbandry.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.husbandry.animals.traits.data.TraitCleanable;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketCleaned extends PenguinPacket {
    private int entityID;
    private boolean cleaned;

    public PacketCleaned(){}
    public PacketCleaned(int entityID, boolean cleaned) {
        this.entityID = entityID;
        this.cleaned = cleaned;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(entityID);
        to.writeBoolean(cleaned);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        entityID = from.readInt();
        cleaned = from.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(entityID);
        if (entity != null) {
            AnimalStats<?> stats = AnimalStats.getStats(entity);
            if (stats != null) {
                ((TraitCleanable)stats.getTrait("cleanable")).setCleaned(cleaned);
            }
        }
    }
}
