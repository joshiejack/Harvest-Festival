package joshie.harvest.player.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Packet.Side.CLIENT)
public class PacketSyncObtained extends PenguinPacket {
    private ItemStack stack;

    public PacketSyncObtained() { }
    public PacketSyncObtained(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        HFTrackers.getClientPlayerTracker().getTracking().addAsObtained(stack);
    }
}