package joshie.harvestmoon.handlers.events;

import static joshie.harvestmoon.helpers.AnimalHelper.feed;
import static joshie.harvestmoon.network.PacketHandler.sendToServer;
import joshie.harvestmoon.animals.AnimalType;
import joshie.harvestmoon.config.Animals;
import joshie.harvestmoon.helpers.AnimalHelper;
import joshie.harvestmoon.helpers.RelationsHelper;
import joshie.harvestmoon.network.PacketSyncCanProduce;
import joshie.harvestmoon.network.PacketSyncRelations;
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

    @SubscribeEvent
    public void onRightClickGround(PlayerInteractEvent event) {
        if (event.action == Action.RIGHT_CLICK_BLOCK) {
            EntityPlayer player = event.entityPlayer;
            if (player.riddenByEntity instanceof EntityChicken) {
                EntityChicken chicken = (EntityChicken) player.riddenByEntity;
                chicken.ridingEntity = null;
                player.riddenByEntity = null;
                chicken.rotationPitch = player.rotationPitch;
                chicken.rotationYaw = player.rotationYaw;
                chicken.moveFlying(0F, 1.0F, 1.25F);
                if (!player.worldObj.isRemote) {
                    AnimalHelper.throwChicken(player, chicken);
                }
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
                    animal.ridingEntity = player;
                    player.riddenByEntity = animal;
                }
                
                RelationsHelper.setTalkedTo(player, animal);
            }
        }
    }

    @SubscribeEvent
    public void onSpawnAttempt(CheckSpawn event) {
        if (!Animals.CAN_SPAWN) {
            if (event.entity instanceof EntityAnimal) {
                event.setResult(Result.DENY);
            }
        }
    }
}
