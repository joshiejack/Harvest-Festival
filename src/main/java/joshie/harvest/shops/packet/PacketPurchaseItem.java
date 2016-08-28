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

@Packet
public class PacketPurchaseItem extends PenguinPacket {
    private int purchaseable_id;

    public PacketPurchaseItem() {}
    public PacketPurchaseItem(IPurchaseable purchaseable) {
        for (int i = 0; i < Shop.allItems.size(); i++) {
            IPurchaseable purchase = Shop.allItems.get(i);
            if (purchase.equals(purchaseable)) {
                purchaseable_id = i;
                break;
            }
        }
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
        IPurchaseable purchaseable = Shop.allItems.get(purchaseable_id);
        if (!player.worldObj.isRemote) {
            if (purchaseable.canBuy(player.worldObj, player)) {
                if (purchase((EntityPlayerMP)player, purchaseable, purchaseable.getCost())) {
                    player.closeScreen();
                }
            }
        } else purchaseable.onPurchased(player);
    }

    private boolean purchase(EntityPlayerMP player, IPurchaseable purchaseable, long cost) {
        StatsServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTracker(player).getStats();
        if (stats.getGold() - cost >= 0) {
            stats.addGold(player, -cost);
            PacketHandler.sendToClient(new PacketPurchaseItem(purchaseable), player); //Send the packet back
            return purchaseable.onPurchased(player);
        }

        return false;
    }
}