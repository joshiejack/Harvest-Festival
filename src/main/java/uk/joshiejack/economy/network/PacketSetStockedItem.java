package uk.joshiejack.economy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.economy.shop.inventory.Inventory;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader(side = Side.CLIENT)
public class PacketSetStockedItem extends AbstractPacketSyncDepartment {
    private Department department;
    private Listing purchasable;
    private int stockID;

    public PacketSetStockedItem() { }
    public PacketSetStockedItem(Department department, Listing purchasable, int stockID) {
        super(department);
        this.purchasable = purchasable;
        this.stockID = stockID;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, purchasable.getID());
        buf.writeInt(stockID);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        purchasable = department.getListingByID(ByteBufUtils.readUTF8String(buf));
        stockID = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        Inventory.get(player.world).getStockForDepartment(department).setStockedItem(purchasable, stockID);
    }
}