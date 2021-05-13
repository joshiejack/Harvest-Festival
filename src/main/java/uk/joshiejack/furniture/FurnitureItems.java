package uk.joshiejack.furniture;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.furniture.item.ItemBasket;

@GameRegistry.ObjectHolder(Furniture.MODID)
@Mod.EventBusSubscriber(modid = Furniture.MODID)
public class FurnitureItems {
    public static final ItemBasket BASKET = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBasket());
    }
}
