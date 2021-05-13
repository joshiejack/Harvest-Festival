package uk.joshiejack.economy.shop.inventory;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.shipping.Market;
import uk.joshiejack.penguinlib.events.NewDayEvent;

@Mod.EventBusSubscriber(modid = Economy.MODID)
public class Restocker {
    @SubscribeEvent
    public static void onNewDay(NewDayEvent event) {
        World world = event.getWorld();
        if (world.provider.getDimension() == 0) {
            Inventory.get(world).newDay(world.rand);
            Market.get(event.getWorld()).newDay(world);
        }
    }
}
