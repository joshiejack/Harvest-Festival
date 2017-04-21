package joshie.harvest.cooking.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.helpers.NBTHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

@Packet(Side.CLIENT)
public class PacketExpandedSlot extends PenguinPacket {
    private int windowId;
    private int slot;
    private ItemStack item;

    public PacketExpandedSlot() {}
    public PacketExpandedSlot(int windowIdIn, int slotIn, @Nullable ItemStack itemIn) {
        this.windowId = windowIdIn;
        this.slot = slotIn;
        this.item = itemIn == null ? null : itemIn.copy();
    }

    @Override
    public void toBytes(ByteBuf buffer) {
        buffer.writeByte(windowId);
        buffer.writeShort(slot);
        if (item == null) buffer.writeBoolean(false);
        else {
            buffer.writeBoolean(true);
            ByteBufUtils.writeTag(buffer, NBTHelper.writeItemStack(item, new NBTTagCompound()));
        }
    }

    @Override
    public void fromBytes(ByteBuf buffer) {
        windowId = buffer.readByte();
        slot = buffer.readShort();
        if (buffer.readBoolean()) {
            item = NBTHelper.readItemStack(ByteBufUtils.readTag(buffer));
        }
    }

    @SideOnly(net.minecraftforge.fml.relauncher.Side.CLIENT)
    @Override
    public void handleQueuedClient(NetHandlerPlayClient handler) {
        handler.handleSetSlot(new SPacketSetSlot(windowId, slot, item));
    }
}
