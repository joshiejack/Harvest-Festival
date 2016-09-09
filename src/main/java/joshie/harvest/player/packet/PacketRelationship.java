package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.logging.log4j.Level;

import java.util.UUID;

public abstract class PacketRelationship extends PenguinPacket {
    private UUID key;
    private boolean particles;

    public PacketRelationship() {}
    public PacketRelationship(UUID key, boolean particles) {
        this.key = key;
        this.particles = particles;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, key.toString());
        buf.writeBoolean(particles);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            key = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            particles = buf.readBoolean();
        } catch (Exception e) { HarvestFestival.LOGGER.log(Level.ERROR, "Failed to read a sync gift packet correctly"); }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (particles) {
            Entity entity = EntityHelper.getAnimalFromUUID(player.worldObj, key);
            if (entity != null) {
                for (int j = 0; j < 3D; j++) {
                    double x = (entity.posX - 0.5D) + player.worldObj.rand.nextFloat();
                    double y = (entity.posY - 0.5D) + player.worldObj.rand.nextFloat();
                    double z = (entity.posZ - 0.5D) + player.worldObj.rand.nextFloat();
                    player.worldObj.spawnParticle(EnumParticleTypes.HEART, x, 1D + y - 0.125D, z, 0, 0, 0);
                }
            }
        }
        
        handleRelationship(player, key);
    }

    protected abstract void handleRelationship(EntityPlayer player, UUID key);
}