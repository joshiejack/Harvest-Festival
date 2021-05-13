package uk.joshiejack.energy.events;

import uk.joshiejack.energy.Energy;
import uk.joshiejack.energy.EnergyEffects;
import uk.joshiejack.penguinlib.data.holder.HolderRegistryList;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import net.minecraft.item.ItemFood;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Energy.MODID)
public class ExhaustionPreventionEvents {
    private static final HolderRegistryList NO_USES = new HolderRegistryList();

    @SubscribeEvent
    public static void onDatbaseLoaded(DatabaseLoadedEvent.LoadComplete event) {
        event.table("exhaustion").rows().forEach(row -> NO_USES.add(row.holder()));
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntityPlayer().isPotionActive(EnergyEffects.EXHAUSTION)
                && NO_USES.contains(event.getItemStack())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getEntityPlayer().isPotionActive(EnergyEffects.EXHAUSTION)
                && NO_USES.contains(event.getItemStack()) && !(event.getItemStack().getItem() instanceof ItemFood)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntityPlayer().isPotionActive(EnergyEffects.EXHAUSTION)
                && NO_USES.contains(event.getItemStack())) {
            event.setCanceled(true);
        }
    }
}
