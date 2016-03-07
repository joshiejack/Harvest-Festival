package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.items.HFItems;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;

public class PacketWateringCan extends AbstractPacketLocation implements IMessageHandler<PacketWateringCan, IMessage> {
    private ItemStack stack;

    public PacketWateringCan() {}
    public PacketWateringCan(ItemStack stack, World world, int x, int y, int z) {
        super(world.provider.dimensionId, x, y, z);
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeItemStack(buf, stack);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public IMessage onMessage(PacketWateringCan message, MessageContext ctx) {
        HFItems.wateringcan.onItemUse(message.stack, ctx.getServerHandler().playerEntity, DimensionManager.getWorld(message.dim), message.x, message.y, message.z, 0, 0, 0, 0);
        return null;
    }
}
