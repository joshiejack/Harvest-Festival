package joshie.harvest.core.handlers.events;

import static joshie.harvest.core.network.PacketHandler.sendToServer;

import java.util.HashSet;

import joshie.harvest.HarvestFestival;
import joshie.harvest.api.animals.IAnimalTracked;
import joshie.harvest.core.config.Animals;
import joshie.harvest.core.helpers.AnimalHelper;
import joshie.harvest.core.helpers.RelationsHelper;
import joshie.harvest.core.network.PacketDismount;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncCanProduce;
import joshie.harvest.core.network.PacketSyncRelations;
import joshie.harvest.npc.EntityNPC;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent.CheckSpawn;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class AnimalEvents {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityLoaded(EntityJoinWorldEvent event) {
        if (event.entity instanceof IAnimalTracked) {
            HarvestFestival.proxy.getAnimalTracker().onJoinWorld(((IAnimalTracked) event.entity).getData());
            if (event.world.isRemote) {
                //Request information about whether you can get products from the cow or sheep
                if (event.entity instanceof EntityCow || event.entity instanceof EntitySheep) {
                    sendToServer(new PacketSyncCanProduce(event.entity.getEntityId(), true));
                }
            }

            /* Disable sheep from ever eating grass */
            if (event.entity instanceof EntitySheep) {
                ((EntitySheep) event.entity).tasks.removeTask(((EntitySheep) event.entity).field_146087_bs);
            } else if (event.entity instanceof EntityChicken) { /* Make chickens take forever to lay eggs */
                ((EntityChicken) event.entity).timeUntilNextEgg = Integer.MAX_VALUE;
            }
        }
        
        if (event.world.isRemote) {
            if (event.entity instanceof EntityAnimal || event.entity instanceof EntityNPC) {
                sendToServer(new PacketSyncRelations(event.entity.getEntityId()));
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.entity instanceof IAnimalTracked) {
            RelationsHelper.removeRelations(event.entityLiving);
            HarvestFestival.proxy.getAnimalTracker().onDeath(((IAnimalTracked) event.entityLiving).getData());
        }
    }

    private HashSet<Entity> mounted = new HashSet();

    @SubscribeEvent
    public void onRightClickGround(PlayerInteractEvent event) {
        if (event.action != Action.LEFT_CLICK_BLOCK && event.entityPlayer.worldObj.isRemote) {
            EntityPlayer player = event.entityPlayer;
            if (player.riddenByEntity instanceof EntityChicken) {
                if (mounted.contains(player.riddenByEntity)) {
                    mounted.remove(player.riddenByEntity);
                    return;
                }

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
    public void onEntityInteract(EntityInteractEvent event) {
        //TODO: Add to entity classes
        /*
        if (event.target instanceof EntityAnimal) {
            EntityPlayer player = event.entityPlayer;
            ItemStack held = player.getCurrentEquippedItem();
            EntityAnimal animal = (EntityAnimal) event.target;
            if (held != null) {
                Item item = held.getItem();
                if (item == Items.bucket || item == Items.glass_bottle || item == Items.wheat_seeds) {
                    event.setCanceled(true);
                } else if (isFood(animal, held)) {
                    feed(player, animal);
                    event.setCanceled(true);
                }
            } else {
                if (animal instanceof EntityChicken) {
                    if (animal.ridingEntity == null) {
                        animal.mountEntity(player);
                        mounted.add(animal);
                    }
                }

                RelationsHelper.setTalkedTo(player, animal);
            }
        } */
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
