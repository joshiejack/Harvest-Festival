package joshie.harvestmoon.core.network;

import io.netty.buffer.ByteBuf;
import joshie.harvestmoon.api.shops.IPurchaseable;
import joshie.harvestmoon.core.helpers.ShopHelper;
import joshie.harvestmoon.shops.ShopInventory;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PacketPurchaseItem implements IMessage, IMessageHandler<PacketPurchaseItem, IMessage> {
    private int purchaseable_id;
    
    public PacketPurchaseItem() {}
    public PacketPurchaseItem(IPurchaseable purchaseable) {
        for(int i = 0; i < ShopInventory.registers.size(); i++) {
            IPurchaseable purchase = ShopInventory.registers.get(i);
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
        this.purchaseable_id = buf.readInt();
    }
    
    @Override
    public IMessage onMessage(PacketPurchaseItem message, MessageContext ctx) {        
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        IPurchaseable purchaseable = ShopInventory.registers.get(message.purchaseable_id);
        if(purchaseable.canBuy(player.worldObj, player)) {
            ShopHelper.purchase(player, purchaseable.getProducts(), purchaseable.getCost());
        }
        
        return null;
    }
}