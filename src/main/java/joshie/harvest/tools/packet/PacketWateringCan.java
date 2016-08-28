package joshie.harvest.tools.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.core.network.PenguinPacketLocation;
import joshie.harvest.core.network.Packet;
import joshie.harvest.tools.HFTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet(Packet.Side.SERVER)
public class PacketWateringCan extends PenguinPacketLocation {
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
    public void handlePacket(EntityPlayer player) {
        HFTools.WATERING_CAN.onItemUse(stack, player, DimensionManager.getWorld(dim), pos, player.getActiveHand(), EnumFacing.DOWN, 0, 0, 0);
    }
}