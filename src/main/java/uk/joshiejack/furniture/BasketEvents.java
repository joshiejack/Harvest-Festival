package uk.joshiejack.furniture;

import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Furniture.MODID)
public class BasketEvents {
    @SubscribeEvent
    public static void onItemPickup(EntityItemPickupEvent event) {
        //TODO: Make the basket work
        /*
        IItemHandler basket = EntityBasket.getBasketForPlayer(event.getEntityPlayer());
        if (basket != null) {
            ItemStack stack = event.getItem().getItem();
            ItemStack result = ItemHandlerHelper.insertItem(basket, stack, true);
            if (result.isEmpty()) {
                event.getItem().setDead();
                event.setCanceled(true);
                ItemHandlerHelper.insertItem(basket, stack, false);
            }
        } */
    }
}
