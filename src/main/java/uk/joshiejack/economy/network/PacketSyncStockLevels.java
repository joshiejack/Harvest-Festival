package uk.joshiejack.economy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.inventory.Inventory;
import uk.joshiejack.economy.shop.inventory.Stock;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.CLIENT)
public class PacketSyncStockLevels extends AbstractPacketSyncDepartment {
    private NBTTagCompound data;

    public PacketSyncStockLevels() {}
    public PacketSyncStockLevels(Department department, Stock stock) {
        super(department);
        this.data = stock.serializeNBT();
    }

    @Override
    public void toBytes(ByteBuf to) {
        super.toBytes(to);
        ByteBufUtils.writeTag(to, data);
    }

    @Override
    public void fromBytes(ByteBuf from) {
        super.fromBytes(from);
        data = ByteBufUtils.readTag(from);
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Inventory.get(player.world).getStockForDepartment(department).deserializeNBT(data);
    }
}
