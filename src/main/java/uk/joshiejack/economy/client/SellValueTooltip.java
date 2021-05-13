package uk.joshiejack.economy.client;

import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.EconomyConfig;
import uk.joshiejack.economy.shipping.ShippingRegistry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Economy.MODID, value = Side.CLIENT)
public class SellValueTooltip {
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        if (EconomyConfig.enableDebug && ShippingRegistry.INSTANCE.getValue(event.getItemStack()) > 0 && event.getFlags().isAdvanced()) {
            event.getToolTip().add("Sell Value: " + ShippingRegistry.INSTANCE.getValue(event.getItemStack()));
        }
    }
}