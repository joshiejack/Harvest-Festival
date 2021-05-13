package uk.joshiejack.economy.shipping;

import uk.joshiejack.economy.Economy;
import uk.joshiejack.economy.event.ItemGetValueEvent;
import uk.joshiejack.penguinlib.data.holder.Holder;
import uk.joshiejack.penguinlib.data.holder.HolderRegistry;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Economy.MODID)
public class ShippingRegistry {
    public static final ShippingRegistry INSTANCE = new ShippingRegistry();
    private final HolderRegistry<Long> registry = new HolderRegistry<>(0L);

    public Set<Map.Entry<Holder, Long>> getEntries() {
        return registry.getEntries();
    }

    @SuppressWarnings("ConstantConditions")
    public long getValue(ItemStack stack) {
        long value = stack.hasTagCompound() && stack.getTagCompound().hasKey("SellValue") ? stack.getTagCompound().getLong("SellValue") : registry.getValue(stack);
        ItemGetValueEvent event = new ItemGetValueEvent(stack, value);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getNewValue();
    }

    public void setValue(Holder holder, long value) {
        registry.register(holder, value);
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("shipping").rows().forEach(shipping -> {
            Holder holder = shipping.holder();
            if (!holder.isEmpty() && !shipping.isEmpty("value")) {
                ShippingRegistry.INSTANCE.setValue(holder, shipping.getAsLong("value"));
            }
        });
    }
}
