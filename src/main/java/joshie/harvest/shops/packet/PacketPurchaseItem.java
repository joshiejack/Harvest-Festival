package joshie.harvest.shops.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.MCClientHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.stats.StatsServer;
import joshie.harvest.shops.data.ShopData;
import joshie.harvest.town.TownHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;

@Packet
public class PacketPurchaseItem extends PenguinPacket {
    private IPurchasable purchasable;
    private Shop shop;
    private int amount;

    public PacketPurchaseItem() {}
    public PacketPurchaseItem(Shop shop, IPurchasable purchasable, int amount) {
        this.shop = shop;
        this.purchasable = purchasable;
        this.amount = amount;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, shop.resourceLocation.toString());
        ByteBufUtils.writeUTF8String(buf, purchasable.getPurchaseableID());
        buf.writeInt(amount);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shop = Shop.REGISTRY.get(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        purchasable = shop.getPurchasableFromID(ByteBufUtils.readUTF8String(buf));
        amount =  buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (purchasable.canDo(player.worldObj, player, amount)) {
                if (purchase((EntityPlayerMP)player)) {
                    PacketHandler.sendToClient(new PacketPurchaseItem(shop, purchasable, amount), player); //Send the packet back
                }
            }
        } else {
            //Purchase the item this many times
            ShopData data = TownHelper.getClosestTownToEntity(player).getShops();
            for (int i = 0; i < amount; i++) {
                data.onPurchasableHandled(player, shop, purchasable);
            }

            //If we can longer be listed, then refresh the gui
            if (!data.canList(shop, purchasable)) {
                MCClientHelper.initGui();
            }
        }
    }

    private boolean purchase(EntityPlayerMP player) {
        StatsServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats();
        long cost = TownHelper.getClosestTownToEntity(player).getShops().getSellValue(shop, purchasable);
        if (stats.getGold() -(cost * amount) >= 0) {
            stats.addGold(player, -(cost * amount));
            for (int i = 0; i < amount; i++) {
                TownHelper.getClosestTownToEntity(player).getShops().onPurchasableHandled(player, shop, purchasable);
            }

            HFTrackers.markTownsDirty();
            return true;
        }

        return false;
    }
}