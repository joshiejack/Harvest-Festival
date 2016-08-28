package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@HFEvents
public class AnimalEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityLoaded(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (!entity.worldObj.isRemote && entity instanceof IAnimalTracked) {
            HFTrackers.<AnimalTrackerServer>getAnimalTracker(event.getWorld()).onJoinWorld(((IAnimalTracked) entity).getData());
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof IAnimalTracked) {
            HFTrackers.getAnimalTracker(event.getEntityLiving().worldObj).onDeath(((IAnimalTracked) event.getEntityLiving()));
        }
    }

    /* When right clicking chickens, will throw any harvest chickens on your head **/
    @HFEvents(Side.CLIENT)
    public static class PickupChicken {
        public static boolean register() { return HFAnimals.PICKUP_CHICKENS; }

        @SubscribeEvent
        public void onRightClickGround(PlayerInteractEvent.RightClickBlock event) {
            EntityPlayer player = event.getEntityPlayer();
            List<Entity> passengers = event.getEntity().getPassengers();
            for (int i = passengers.size() - 1; i >= 0; --i) {
                Entity entity = passengers.get(i);
                if (entity instanceof IAnimalTracked) {
                    ((IAnimalTracked)entity).getData().dismount(player);
                }

                entity.removePassengers();
                entity.dismountRidingEntity();
                entity.rotationPitch = player.rotationPitch;
                entity.rotationYaw = player.rotationYaw;
                entity.moveRelative(0F, 1.0F, 1.05F);
            }
        }
    }

    /* Disables vanilla cows, chickens and sheep from spawning naturally if spawning is disabled **/
    @HFEvents
    public static class SpawnAttempt {
        public static boolean register() { return !HFAnimals.CAN_SPAWN; }

        @SubscribeEvent
        public void onSpawnAttempt(CheckSpawn event) {
            Class<? extends Entity> animal = event.getEntity().getClass();
            if (animal == EntityCow.class || animal == EntitySheep.class || animal == EntityChicken.class) {
                event.setResult(Result.DENY);
            }
        }
    }
}