package uk.joshiejack.economy.shop;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.economy.api.shops.Comparator;
import uk.joshiejack.economy.api.shops.Condition;
import uk.joshiejack.economy.api.shops.ListingHandler;
import uk.joshiejack.economy.shop.builder.ListingBuilder;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;

import static uk.joshiejack.economy.Economy.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class EconomyShops {
    @SubscribeEvent
    public static void onCollectForRegistration(CollectRegistryEvent event) {
        event.add(Comparator.class, (d, c, s, l) -> Comparator.register(l, c.newInstance()));
        event.add(Condition.class, ((d, c, s, l) -> Condition.register(l, c.newInstance())));
        event.add(ListingHandler.class, (((d, c, s, l) -> Listing.HANDLERS.put(l, c.newInstance()))));
        event.add(ListingBuilder.class, (((d, c, s, l) -> ListingBuilder.BUILDERS.put(l, c.newInstance()))));
    }
}
