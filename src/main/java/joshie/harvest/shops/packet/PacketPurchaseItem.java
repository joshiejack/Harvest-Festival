package joshie.harvest.shops.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.shops.IPurchasable;
import joshie.harvest.core.handlers.HFTrackers;
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

    public PacketPurchaseItem() {}
    public PacketPurchaseItem(Shop shop, IPurchasable purchasable) {
        this.shop = shop;
        this.purchasable = purchasable;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, shop.resourceLocation.toString());
        ByteBufUtils.writeUTF8String(buf, purchasable.getPurchaseableID());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        shop = ShopRegistry.INSTANCE.getShop(new ResourceLocation(ByteBufUtils.readUTF8String(buf)));
        purchasable = shop.getPurchasableFromID(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            if (purchasable.canBuy(player.worldObj, player)) {
                if (purchase((EntityPlayerMP)player, shop, purchasable, purchasable.getCost())) {
                    player.closeScreen();
                }
            }
        } else purchasable.onPurchased(player);
    }

    private boolean purchase(EntityPlayerMP player, Shop shop, IPurchasable purchaseable, long cost) {
        StatsServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats();
        if (stats.getGold() - cost >= 0) {
            stats.addGold(player, -cost);
            PacketHandler.sendToClient(new PacketPurchaseItem(shop, purchaseable), player); //Send the packet back
            return purchaseable.onPurchased(player);
        }

        return false;
    }
}