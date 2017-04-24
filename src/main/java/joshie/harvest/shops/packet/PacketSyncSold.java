package joshie.harvest.shops.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PacketNBT;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

@Packet(Side.CLIENT)
public class PacketSyncSold extends PacketNBT {
    private UUID uuid;

    public PacketSyncSold() {}
    public PacketSyncSold(UUID uuid, NBTTagCompound tag) {
        super(tag);
        this.uuid = uuid;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        ByteBufUtils.writeUTF8String(to, uuid.toString());
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        uuid = UUID.fromString(ByteBufUtils.readUTF8String(from));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TownData data = TownHelper.getTownByID(player.world, uuid);
        if (data != null) {
            data.getShops().readFromNBT(tag);
        }
    }
}