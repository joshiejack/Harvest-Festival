package joshie.harvest.shops.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.stats.StatsServer;
import joshie.harvest.shops.Shop;
import joshie.harvest.shops.ShopRegistry;
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
        buf.writeByte(amount);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shop = ShopRegistry.INSTANCE.getShop(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        purchasable = shop.getPurchasableFromID(ByteBufUtils.readUTF8String(buf));
        amount =  buf.readByte();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (purchasable.canBuy(player.worldObj, player, amount)) {
                if (purchase((EntityPlayerMP)player)) {
                    PacketHandler.sendToClient(new PacketPurchaseItem(shop, purchasable, amount), player); //Send the packet back
                }
            }
        } else {
            //Purchase the item this many times
            for (int i = 0; i < amount; i++) {
                purchasable.onPurchased(player);
            }
        }
    }

    private boolean purchase(EntityPlayerMP player) {
        StatsServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats();
        if (stats.getGold() -(purchasable.getCost() * amount) >= 0) {
            stats.addGold(player, -(purchasable.getCost() * amount));
            for (int i = 0; i < amount; i++) purchasable.onPurchased(player);
            return true;
        }

        return false;
    }
}