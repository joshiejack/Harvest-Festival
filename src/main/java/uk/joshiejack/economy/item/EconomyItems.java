package uk.joshiejack.economy.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.economy.Economy;

@SuppressWarnings("unused")
@GameRegistry.ObjectHolder(Economy.MODID)
@Mod.EventBusSubscriber(modid = Economy.MODID)
public class EconomyItems {
    public static final ItemManager MANAGER = null;

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemManager());
    }
}
