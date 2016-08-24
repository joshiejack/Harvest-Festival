package joshie.harvest.shops.packets;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.helpers.ShopHelper;
import joshie.harvest.core.network.Packet;
import joshie.harvest.core.network.Packet.Side;
import joshie.harvest.core.network.PenguinPacket;
import joshie.harvest.shops.Shop;
import net.minecraft.entity.player.EntityPlayerMP;

@Packet(Side.SERVER)
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
    public boolean handleServerPacket(EntityPlayerMP player) {
        IPurchaseable purchaseable = Shop.allItems.get(purchaseable_id);
        if (purchaseable.canBuy(player.worldObj, player)) {
            if (ShopHelper.purchase(player, purchaseable, purchaseable.getCost())) {
                player.closeScreen();
            }
        }

        return true;
    }
}