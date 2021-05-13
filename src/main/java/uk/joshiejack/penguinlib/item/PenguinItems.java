package uk.joshiejack.penguinlib.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import uk.joshiejack.penguinlib.PenguinConfig;
import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.base.ItemSpecial;

@GameRegistry.ObjectHolder(PenguinLib.MOD_ID)
@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class PenguinItems {
    public static final ItemEntity ENTITY = null;
    public static final ItemSpecial SPECIAL = null;
    public static final ItemDinnerware DINNERWARE = null;

    @SubscribeEvent
    public static void registerItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(new ItemEntity(), new ItemSpecial());
        if (PenguinConfig.forceDinnerwareItem || PenguinConfig.requireDishes) event.getRegistry().register(new ItemDinnerware());
        if (PenguinConfig.enableDebuggingTools) {
            event.getRegistry().register(new ItemTools());
        }
    }
}