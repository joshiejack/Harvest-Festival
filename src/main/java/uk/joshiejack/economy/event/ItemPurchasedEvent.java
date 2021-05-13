package uk.joshiejack.economy.event;

import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class ItemPurchasedEvent extends PlayerEvent {
    private final Department shop;
    private final Listing purchasable;

    public ItemPurchasedEvent(EntityPlayer player, Department shop, Listing purchasable) {
        super(player);
        this.shop = shop;
        this.purchasable = purchasable;
    }

    public Department getShop() {
        return shop;
    }

    public Listing getPurchasable() {
        return purchasable;
    }
}
