package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.tile.inventory.TileInventory;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

@PenguinLoader(side = Side.CLIENT)
public class PacketSetInventorySlot extends PacketRenderBlockUpdate {
    private int slot;
    private ItemStack stack;

    public PacketSetInventorySlot() {}
    public PacketSetInventorySlot(BlockPos pos, int slot, ItemStack stack) {
        super(pos);
        this.slot = slot;
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        to.writeInt(slot);
        ByteBufUtils.writeItemStack(to, stack);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        slot = from.readInt();
        stack = ByteBufUtils.readItemStack(from);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof TileInventory) {
            IItemHandler handler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
            if (handler instanceof ItemStackHandler) {
                ((ItemStackHandler) handler).setStackInSlot(slot, stack);
            }
        }

        super.handlePacket(player);
    }
}
