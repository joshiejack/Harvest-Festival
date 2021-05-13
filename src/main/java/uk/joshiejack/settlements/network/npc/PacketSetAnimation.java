package uk.joshiejack.settlements.network.npc;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.settlements.entity.EntityNPC;
import uk.joshiejack.penguinlib.network.packet.PacketSyncNBTTagCompound;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSetAnimation extends PacketSyncNBTTagCompound {
    private int npcID;
    private String animation;

    public PacketSetAnimation() {}
    public PacketSetAnimation(int npcID, String animation, NBTTagCompound data) {
        super(data);
        this.npcID = npcID;
        this.animation = animation;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeInt(npcID);
        ByteBufUtils.writeUTF8String(buf, animation);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        npcID = buf.readInt();
        animation = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Entity entity = player.world.getEntityByID(npcID);
        if (entity instanceof EntityNPC) {
            ((EntityNPC)entity).setAnimation(animation, tag);
        }
    }
}
