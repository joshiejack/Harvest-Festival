package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.HarvestFestival;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.apache.logging.log4j.Level;

import java.util.UUID;

public abstract class PacketRelationship extends PenguinPacket {
    private UUID key;
    private EnumParticleTypes particles;

    public PacketRelationship() {}
    public PacketRelationship(UUID key, EnumParticleTypes particles) {
        this.key = key;
        this.particles = particles;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, key.toString());
        buf.writeBoolean(particles != null);
        if (particles != null) {
            buf.writeByte(particles.getParticleID());
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            key = UUID.fromString(ByteBufUtils.readUTF8String(buf));
            if (buf.readBoolean()) {
                particles = EnumParticleTypes.getParticleFromId(buf.readByte());
            }
        } catch (Exception e) { HarvestFestival.LOGGER.log(Level.ERROR, "Failed to read a sync gift packet correctly"); }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (particles != null) {
            EntityAnimal entity = EntityHelper.getAnimalFromUUID(player.worldObj, key);
            if (entity != null) {
                if (particles == EnumParticleTypes.HEART) {
                    for (int j = 0; j < 3D; j++) {
                        double x = (entity.posX - 0.5D) + player.worldObj.rand.nextFloat();
                        double y = (entity.posY - 0.5D) + player.worldObj.rand.nextFloat();
                        double z = (entity.posZ - 0.5D) + player.worldObj.rand.nextFloat();
                        player.worldObj.spawnParticle(particles, x, 1D + y - 0.125D, z, 0, 0, 0);
                    }
                } else {
                    for (int j = 0; j < 16D; j++) {
                        double x = (entity.posX - 0.5D) + player.worldObj.rand.nextFloat();
                        double y = (entity.posY - 0.5D) + player.worldObj.rand.nextFloat();
                        double z = (entity.posZ - 0.5D) + player.worldObj.rand.nextFloat();
                        player.worldObj.spawnParticle(particles, x, 1 + y, z, 0, 0, 0);
                    }
                }
            }
        }
        
        handleRelationship(player, key);
    }

    protected abstract void handleRelationship(EntityPlayer player, UUID key);
}