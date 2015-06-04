package joshie.harvest.core.handlers.events;

import static joshie.harvest.core.helpers.AnimalHelper.feed;
import static joshie.harvest.core.network.PacketHandler.sendToServer;

import java.util.HashSet;

import joshie.harvest.animals.AnimalType;
import joshie.harvest.core.config.Animals;
import joshie.harvest.core.helpers.AnimalHelper;
import joshie.harvest.core.helpers.RelationsHelper;
import joshie.harvest.core.network.PacketDismountChicken;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncCanProduce;
import joshie.harvest.core.network.PacketSyncRelations;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        if (event.entity instanceof EntityAnimal) {
            if (!event.world.isRemote) AnimalHelper.onJoinWorld((EntityAnimal) event.entity);
            if (event.world.isRemote) {
                //Request information about whether you can get products from the cow or sheep
                if (event.entity instanceof EntityCow || event.entity instanceof EntitySheep) {
                    sendToServer(new PacketSyncCanProduce(event.entity.getEntityId(), true));
                }

                sendToServer(new PacketSyncRelations(event.entity.getEntityId()));
            }

            /* Disable sheep from ever eating grass */
            if (event.entity instanceof EntitySheep) {
                ((EntitySheep) event.entity).tasks.removeTask(((EntitySheep) event.entity).field_146087_bs);
            } else if (event.entity instanceof EntityChicken) { /* Make chickens take forever to lay eggs */
                ((EntityChicken) event.entity).timeUntilNextEgg = Integer.MAX_VALUE;
            }
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (event.entity instanceof EntityAnimal) {
            RelationsHelper.removeRelations(event.entityLiving);
            AnimalHelper.onDeath((EntityAnimal) event.entityLiving);
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
                PacketHandler.sendToServer(new PacketDismountChicken());
            }
        }
    }

    //If it's a food item
    public boolean isFood(EntityAnimal animal, ItemStack item) {
        return AnimalType.getType(animal).canEat(item);
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
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
        }
    }

    @SubscribeEvent
    public void onSpawnAttempt(CheckSpawn event) {
        if (!Animals.CAN_SPAWN) {
            if (event.entity instanceof EntityCow || event.entity instanceof EntitySheep || event.entity instanceof EntityChicken) {
                event.setResult(Result.DENY);
            }
        }
    }
}
