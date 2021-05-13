package uk.joshiejack.economy.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import uk.joshiejack.economy.gold.Bank;
import uk.joshiejack.economy.gold.Vault;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.economy.shop.ShopHelper;
import uk.joshiejack.economy.shop.inventory.Inventory;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader
public class PacketPurchaseItem extends AbstractPacketSyncDepartment {
    private Listing purchasable;
    private int amount;

    public PacketPurchaseItem() {}
    public PacketPurchaseItem(Department department, Listing purchasable, int amount) {
        super(department);
        this.purchasable = purchasable;
        this.amount = amount;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        ByteBufUtils.writeUTF8String(buf, purchasable.getID());
        buf.writeInt(amount);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        purchasable = department.getListingByID(ByteBufUtils.readUTF8String(buf));
        amount =  buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.world.isRemote) {
            if (purchasable.canPurchase(player, Inventory.get(player.world).getStockForDepartment(department), amount)) {
                if (purchase((EntityPlayerMP)player)) {
                    PenguinNetwork.sendToClient(new PacketPurchaseItem(department, purchasable, amount), player); //Send the packet back
                }
            }
        } else {
            for (int i = 0; i < amount; i++) {
                purchasable.purchase(player);
            }

            ShopHelper.resetGUI();

            //TODO: Re-enable limited inventory
            /*
            //Purchase the item this many times
            ShopData data = TownHelper.getClosestTownToEntity(player, false).getShops();
            for (int i = 0; i < cost; i++) {
                data.onPurchasableHandled(player, shop, purchasable);
            }

            //If we can longer be listed, then refresh the gui
            if (!data.canList(shop, purchasable)) {
                MCClientHelper.initGui();
            } */
        }
    }

    private boolean purchase(EntityPlayerMP player) {
        Vault vault = Bank.get(player.world).getVaultForPlayer(player);
        long cost = purchasable.getGoldCost(player, Inventory.get(player.world).getStockForDepartment(department)); //TownHelper.getClosestTownToEntity(player, false).getShops().getSellValue(shop, purchasable); //TODO: Enable adjusted value
        long total = cost * amount;
        if (vault.getBalance() - total >= 0) {
            if (total >= 0) vault.decreaseGold(player.world, total);
            else vault.increaseGold(player.world, total);
            for (int i = 0; i < amount; i++) {
                purchasable.purchase(player); //TODO: Call handler and stuff
                //TownHelper.getClosestTownToEntity(player, false).getShops().onPurchasableHandled(player, shop, purchasable);
            }

            //HFTrackers.markTownsDirty();
            return true;
        }

        return false;
    }
}