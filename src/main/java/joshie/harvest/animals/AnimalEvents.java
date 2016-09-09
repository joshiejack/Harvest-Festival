package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@HFEvents
public class AnimalEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityLoaded(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (!entity.worldObj.isRemote && entity instanceof IAnimalTracked) {
            HFTrackers.<AnimalTrackerServer>getAnimalTracker(event.getWorld()).onJoinWorld((AnimalData)((IAnimalTracked) entity).getData());
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof IAnimalTracked) {
            HFTrackers.getAnimalTracker(event.getEntityLiving().worldObj).onDeath(((IAnimalTracked) event.getEntityLiving()));
        }
    }

    /* When right clicking chickens, will throw any harvest chickens on your head **/
    @HFEvents
    public static class PickupChicken {
        public static boolean register() { return HFAnimals.PICKUP_CHICKENS; }

        @SubscribeEvent
        public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.isBeingRidden() && player.getHeldItemMainhand() == null) {
                Entity entity = event.getTarget();
                if (entity instanceof EntityChicken) {
                    entity.startRiding(player, true);
                }
            }
        }

        @SubscribeEvent
        public void onRightClickGround(PlayerInteractEvent.RightClickBlock event) {
            EntityPlayer player = event.getEntityPlayer();
            for (Entity entity: player.getPassengers()) {
                if (entity instanceof EntityChicken) {
                    entity.dismountRidingEntity();
                    entity.rotationPitch = player.rotationPitch;
                    entity.rotationYaw = player.rotationYaw;
                    entity.moveRelative(0F, 0.5F, 1.05F);
                    if (entity instanceof IAnimalTracked) {
                        ((IAnimalTracked)entity).getData().dismount(player);
                    }
                }
            }
        }
    }
}