package uk.joshiejack.economy.scripting;

import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.event.ItemPurchasedEvent;
import uk.joshiejack.economy.scripting.wrappers.DepartmentJS;
import uk.joshiejack.economy.scripting.wrappers.ListingJS;
import uk.joshiejack.economy.scripting.wrappers.ShopJS;
import uk.joshiejack.economy.scripting.wrappers.SublistingJS;
import uk.joshiejack.economy.shop.Department;
import uk.joshiejack.economy.shop.Listing;
import uk.joshiejack.economy.shop.Shop;
import uk.joshiejack.economy.shop.Sublisting;
import uk.joshiejack.penguinlib.events.CollectScriptingWrappers;
import uk.joshiejack.penguinlib.scripting.event.CollectScriptingMethods;
import uk.joshiejack.penguinlib.scripting.event.ScriptingTriggerFired;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Economy.MODID)
public class EconomyScripting {
    @SubscribeEvent
    public static void onCollectWrappers(CollectScriptingWrappers event) {
        event.register(DepartmentJS.class, Department.class);
        event.register(ListingJS.class, Listing.class);
        event.register(ShopJS.class, Shop.class);
        event.register(SublistingJS.class, Sublisting.class);
    }

    @SubscribeEvent
    public static void onCollectScriptingMethods(CollectScriptingMethods event) {
        event.add("onItemPurchased");
    }

    @SubscribeEvent
    public static void onItemPurchased(ItemPurchasedEvent event) {
        MinecraftForge.EVENT_BUS.post(new ScriptingTriggerFired("onItemPurchased", event.getEntityPlayer(), event.getPurchasable()));
    }
}
