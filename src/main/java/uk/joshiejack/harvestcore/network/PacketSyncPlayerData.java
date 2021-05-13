package uk.joshiejack.harvestcore.network;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.network.packet.PacketSyncNBTTagCompound;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@PenguinLoader
public class PacketSyncPlayerData extends PacketSyncNBTTagCompound {
    private String item;

    public PacketSyncPlayerData() {}
    public PacketSyncPlayerData(EntityPlayer player, String item) {
        super(player.getEntityData().getCompoundTag(item));
        this.item = item;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        ByteBufUtils.writeUTF8String(to, item);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        item = ByteBufUtils.readUTF8String(from);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (player.world.isRemote || item.equals("NotesRead"))
            player.getEntityData().setTag(item, tag);
    }
}
