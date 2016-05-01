package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.items.HFItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketWateringCan extends AbstractPacketLocation implements IMessageHandler<PacketWateringCan, IMessage> {
    private ItemStack stack;

    public PacketWateringCan() {
    }

    public PacketWateringCan(ItemStack stack, World world, BlockPos pos) {
        super(world.provider.getDimension(), pos);
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
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        HFItems.WATERING_CAN.onItemUse(message.stack, player, DimensionManager.getWorld(message.dim), message.pos, player.getActiveHand(), EnumFacing.DOWN, 0, 0, 0);
        return null;
    }
}