package joshie.harvest.animals.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.animals.AnimalStats;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;

@Packet(Side.CLIENT)
public class PacketSyncHappiness extends PenguinPacket {
    private int id;
    private int happiness;

    public PacketSyncHappiness() {}

    public PacketSyncHappiness(int id, int happiness) {
        this.id = id;
        this.happiness = happiness;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(happiness);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = buf.readInt();
        happiness = buf.readInt();
    }

    @Override
    @SuppressWarnings("unchecked, ConstantConditions")
    public void handlePacket(EntityPlayer player) {
        EntityAnimal animal = getEntityAsAnimal();
        if (animal != null) {
            AnimalStats stats = EntityHelper.getStats(animal);
            if (stats != null) {
                stats.affectHappiness(happiness);
                if (happiness > 0) {
                    for (int j = 0; j < 3D; j++) {
                        double x = (animal.posX - 0.5D) + animal.worldObj.rand.nextFloat();
                        double y = (animal.posY - 0.5D) + animal.worldObj.rand.nextFloat();
                        double z = (animal.posZ - 0.5D) + animal.worldObj.rand.nextFloat();
                        animal.worldObj.spawnParticle(EnumParticleTypes.HEART, x, 1D + y - 0.125D, z, 0, 0, 0);
                    }
                } else if (happiness < 0) {
                    for (int j = 0; j < 16D; j++) {
                        double x = (animal.posX - 0.5D) + animal.worldObj.rand.nextFloat();
                        double y = (animal.posY - 0.5D) + animal.worldObj.rand.nextFloat();
                        double z = (animal.posZ - 0.5D) + animal.worldObj.rand.nextFloat();
                        animal.worldObj.spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, x, 1 + y, z, 0, 0, 0);
                    }
                }
            }
        }
    }

    private EntityAnimal getEntityAsAnimal() {
        return (EntityAnimal) MCClientHelper.getWorld().getEntityByID(id);
    }
}