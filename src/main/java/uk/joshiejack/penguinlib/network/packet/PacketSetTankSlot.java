package uk.joshiejack.penguinlib.network.packet;

import io.netty.buffer.ByteBuf;
import uk.joshiejack.penguinlib.block.interfaces.ITankProvider;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;

@PenguinLoader(side = Side.CLIENT)
public class PacketSetTankSlot extends PenguinPacket {
    private BlockPos pos;
    private int slot;
    private FluidStack stack;

    public PacketSetTankSlot() {}
    public PacketSetTankSlot(BlockPos pos, int slot, FluidStack stack) {
        this.pos = pos;
        this.slot = slot;
        this.stack = stack;
    }

    @Override
    public void toBytes(ByteBuf to) {
        to.writeLong(pos.toLong());
        to.writeInt(slot);
        if (stack == null) to.writeBoolean(false);
        else {
            to.writeBoolean(true);
            ByteBufUtils.writeTag(to, stack.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void fromBytes(ByteBuf from) {
        pos = BlockPos.fromLong(from.readLong());
        slot = from.readInt();
        boolean hasStack = from.readBoolean();
        if (hasStack) {
            stack = FluidStack.loadFluidStackFromNBT(ByteBufUtils.readTag(from));
        }
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        TileEntity tile = player.world.getTileEntity(pos);
        if (tile instanceof ITankProvider) {
            ((ITankProvider)tile).setTank(slot, stack);
        }
    }
}
