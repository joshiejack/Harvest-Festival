package joshie.harvest.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvest.api.shops.IPurchaseable;
import joshie.harvest.core.helpers.ShopHelper;
import joshie.harvest.shops.Shop;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketPurchaseItem implements IMessage, IMessageHandler<PacketPurchaseItem, IMessage> {
    private int purchaseable_id;
    
    public PacketPurchaseItem() {}
    public PacketPurchaseItem(IPurchaseable purchaseable) {        
        for(int i = 0; i < Shop.registers.size(); i++) {
            IPurchaseable purchase = Shop.registers.get(i);
            if(purchase.equals(purchaseable)) {
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
    public IMessage onMessage(PacketPurchaseItem message, MessageContext ctx) {        
        EntityPlayerMP player = ctx.getServerHandler().playerEntity;
        IPurchaseable purchaseable = Shop.registers.get(message.purchaseable_id);
        if(purchaseable.canBuy(player.worldObj, player)) {
            if(ShopHelper.purchase(player, purchaseable, purchaseable.getCost())) {
                player.closeScreen();
            }
        }
        
        return null;
    }
}