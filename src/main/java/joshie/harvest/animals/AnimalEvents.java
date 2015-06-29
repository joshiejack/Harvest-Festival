package joshie.harvest.animals;

import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.config.Animals;
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
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AnimalEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityLoaded(EntityJoinWorldEvent event) {
        Entity entity = event.entity;
        if (entity instanceof IAnimalTracked) {
            HFTrackers.getAnimalTracker().onJoinWorld(((IAnimalTracked) entity).getData());
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.entityLiving instanceof IAnimalTracked) {
            HFTrackers.getAnimalTracker().onDeath(((IAnimalTracked) event.entityLiving));
        }
    }

    @SubscribeEvent
    public void onRightClickGround(PlayerInteractEvent event) {
        if (event.action != Action.LEFT_CLICK_BLOCK && event.entityPlayer.worldObj.isRemote) {
            EntityPlayer player = event.entityPlayer;
            if (player.riddenByEntity instanceof EntityChicken) {
                EntityChicken chicken = (EntityChicken) player.riddenByEntity;
                chicken.mountEntity(null);
                chicken.rotationPitch = player.rotationPitch;
                chicken.rotationYaw = player.rotationYaw;
                chicken.moveFlying(0F, 1.0F, 1.25F);
                PacketHandler.sendToServer(new PacketDismount());
            }
        }
    }

    @SubscribeEvent
    public void onSpawnAttempt(CheckSpawn event) {
        if (!Animals.CAN_SPAWN) {
            Class animal = event.entity.getClass();
            if (animal == EntityCow.class || animal == EntitySheep.class || animal == EntityChicken.class) {
                event.setResult(Result.DENY);
            }
        }
    }
}
