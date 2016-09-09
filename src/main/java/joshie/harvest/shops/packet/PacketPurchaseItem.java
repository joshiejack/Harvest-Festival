package joshie.harvest.shops.packet;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.stats.StatsServer;
import joshie.harvest.shops.Shop;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;

@Packet
public class PacketPurchaseItem extends PenguinPacket {
    private int purchaseable_id;

    public PacketPurchaseItem() {}
    public PacketPurchaseItem(ResourceLocation resourceLocation, IPurchaseable purchaseable) {
        ResourceLocation shop = Shop.getRegistryName(resourceLocation, purchaseable);
        purchaseable_id = Shop.REGISTRY.getValues().indexOf(Shop.REGISTRY.getValue(shop));
    }

    public PacketPurchaseItem(int purchaseable_id) {
        this.purchaseable_id = purchaseable_id;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(purchaseable_id);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        purchaseable_id = buf.readInt();
    }

    @Override
    public void handlePacket(EntityPlayer player) {
        IPurchaseable purchaseable = Shop.REGISTRY.getValues().get(purchaseable_id).getPurchaseable();
        if (!player.worldObj.isRemote) {
            if (purchaseable.canBuy(player.worldObj, player)) {
                if (purchase((EntityPlayerMP)player, purchaseable_id, purchaseable, purchaseable.getCost())) {
                    player.closeScreen();
                }
            }
        } else purchaseable.onPurchased(player);
    }

    private boolean purchase(EntityPlayerMP player, int purchaseable_id, IPurchaseable purchaseable, long cost) {
        StatsServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats();
        if (stats.getGold() - cost >= 0) {
            stats.addGold(player, -cost);
            PacketHandler.sendToClient(new PacketPurchaseItem(purchaseable_id), player); //Send the packet back
            return purchaseable.onPurchased(player);
        }

        return false;
    }
}