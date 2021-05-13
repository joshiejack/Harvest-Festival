package uk.joshiejack.husbandry.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketHeartParticle extends PenguinPacket {
    private int entityID;
    private boolean positive;

    public PacketHeartParticle(){}
    public PacketHeartParticle(int entityID, boolean positive) {
        this.entityID = entityID;
        this.positive = positive;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeInt(entityID);
        to.writeBoolean(positive);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        entityID = from.readInt();
        positive = from.readBoolean();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        World world = player.world;
        Entity entity = world.getEntityByID(entityID);
        if (entity != null) {
            EnumParticleTypes type = positive ? EnumParticleTypes.HEART : EnumParticleTypes.DAMAGE_INDICATOR;
            int times = positive ? 3 : 16;
            double offset = positive ? -0.125D : 0D;
            for (int j = 0; j < times; j++) {
                double x = (entity.posX - 0.5D) + world.rand.nextFloat();
                double y = (entity.posY - 0.5D) + world.rand.nextFloat();
                double z = (entity.posZ - 0.5D) + world.rand.nextFloat();
                world.spawnParticle(type, x, 1D + y + offset, z, 0, 0, 0);
            }
        }
    }
}
