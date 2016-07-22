package joshie.harvest.animals;

import joshie.harvest.api.HFRegister;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketDismount;
import joshie.harvest.core.network.PacketHandler;
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

@HFRegister(data = "events")
public class AnimalEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityLoaded(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof IAnimalTracked) {
            HFTrackers.getAnimalTracker(event.getWorld()).onJoinWorld(((IAnimalTracked) entity).getData());
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntityLiving() instanceof IAnimalTracked) {
            HFTrackers.getAnimalTracker(event.getEntityLiving().worldObj).onDeath(((IAnimalTracked) event.getEntityLiving()));
        }
    }

    /* When right clicking chickens, will place them on the players head **/
    //TODO: Allow stack of doom (mount chickens on chickens)
    @HFRegister(data = "events")
    public static class PickupChicken {
        public static boolean register() { return HFAnimals.PICKUP_CHICKENS; }

        @SubscribeEvent
        public void onRightClickGround(PlayerInteractEvent.LeftClickBlock event) {
            if (event.getEntityPlayer().worldObj.isRemote) {
                EntityPlayer player = event.getEntityPlayer();
                if (player.getRidingEntity() instanceof EntityChicken) {
                    EntityChicken chicken = (EntityChicken) player.getRidingEntity();
                    chicken.startRiding(null);
                    chicken.rotationPitch = player.rotationPitch;
                    chicken.rotationYaw = player.rotationYaw;
                    chicken.moveRelative(0F, 1.0F, 1.25F);
                    PacketHandler.sendToServer(new PacketDismount());
                }
            }
        }
    }

    /* Disables vanilla cows, chickens and sheep from spawning naturally if spawning is disabled **/
    @HFRegister(data = "events")
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