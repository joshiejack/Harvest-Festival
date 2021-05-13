package uk.joshiejack.husbandry.animals;

import uk.joshiejack.husbandry.animals.stats.AnimalStats;
import uk.joshiejack.penguinlib.util.helpers.minecraft.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static uk.joshiejack.husbandry.Husbandry.MODID;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = MODID)
public class AnimalInteract {
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntityPlayer().isBeingRidden() && event.getHand() == EnumHand.MAIN_HAND) {
            dismountAnimals(event.getEntityPlayer());
        } else {
            AnimalStats<?> stats = AnimalStats.getStats(event.getTarget());
            if (stats != null) {
                boolean canceled = stats.onRightClick(event.getEntityPlayer(), event.getHand());
                if (canceled) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRightClickGround(PlayerInteractEvent.RightClickBlock event) {
        if (!EntityHelper.forbidsEntityDrops(event.getWorld().getBlockState(event.getPos()).getBlock())) {
            dismountAnimals(event.getEntityPlayer());
        }
    }

    @SubscribeEvent //Automatically dismount any entities than are on top of a player
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        dismountAnimals(event.player);
    }

    private static void dismountUnvalidated(EntityPlayer player) {
        player.getPassengers()
                .forEach(entity -> {
                    entity.dismountRidingEntity();
                    entity.rotationPitch = player.rotationPitch;
                    entity.rotationYaw = player.rotationYaw;
                    entity.moveRelative(0F, 0.1F, 1.05F, 0.1F);
                    entity.setEntityInvulnerable(false);
                });
    }

    private static void dismountAnimals(EntityPlayer player) {
        player.getPassengers().stream()
                .filter(entity -> AnimalStats.getStats(entity) != null)
                .forEach(entity -> {
                    entity.dismountRidingEntity();
                    entity.rotationPitch = player.rotationPitch;
                    entity.rotationYaw = player.rotationYaw;
                    entity.moveRelative(0F, 0.1F, 1.05F, 0.1F);
                    entity.setEntityInvulnerable(false);
                });
    }
}
