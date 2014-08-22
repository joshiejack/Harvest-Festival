package harvestmoon.handlers.events;

import static harvestmoon.HarvestMoon.handler;
import static harvestmoon.helpers.AnimalHelper.canProduceProduct;
import static harvestmoon.helpers.AnimalHelper.setProducedProduct;
import static harvestmoon.helpers.RelationsHelper.getRelationship;
import static harvestmoon.helpers.SizeableHelper.getDamage;
import static harvestmoon.network.PacketHandler.sendToServer;
import harvestmoon.helpers.RelationsHelper;
import harvestmoon.helpers.ToolHelper;
import harvestmoon.init.HMItems;
import harvestmoon.lib.SizeableMeta;
import harvestmoon.lib.SizeableMeta.Size;
import harvestmoon.network.PacketSyncCanProduce;
import harvestmoon.network.PacketSyncRelations;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
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
            if (!event.entity.worldObj.isRemote) {
                handler.getServer().getAnimalTracker().onDeath((EntityAnimal) event.entityLiving);
            } else {
                handler.getClient().getAnimalTracker().onDeath((EntityAnimal) event.entityLiving);
            }
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
                if(!player.worldObj.isRemote) {
                    if(handler.getServer().getAnimalTracker().setThrown(chicken)) {
                        handler.getServer().getPlayerData(player).affectRelationship(chicken, 25);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEntityInteract(EntityInteractEvent event) {
        if (event.target instanceof EntityAnimal) {
            EntityPlayer player = event.entityPlayer;
            ItemStack held = player.getCurrentEquippedItem();
            EntityAnimal animal = (EntityAnimal) event.target;
            if (held != null) {
                if (held.getItem() == Items.wheat) {
                    if (!player.worldObj.isRemote) {
                        if (handler.getServer().getAnimalTracker().setFed(animal)) {
                            handler.getServer().getPlayerData(player).affectRelationship(animal, 5);
                        }
                    }
                    
                    return;
                } else if (ToolHelper.isBrush(held) && !(animal instanceof EntityChicken)) {
                    if (!player.worldObj.isRemote) {
                        if (handler.getServer().getAnimalTracker().setCleaned(animal)) {
                            handler.getServer().getPlayerData(player).affectRelationship(animal, 25);
                        }
                    } else {
                        for (int j = 0; j < 30D; j++) {
                            double d7 = (animal.posY - 0.5D) + animal.worldObj.rand.nextFloat();
                            double d8 = (animal.posX - 0.5D) + animal.worldObj.rand.nextFloat();
                            double d9 = (animal.posZ - 0.5D) + animal.worldObj.rand.nextFloat();
                            animal.worldObj.spawnParticle("townaura", d8, 1.0D + d7 - 0.125D, d9, 0, 0, 0);
                        }
                    }

                    return;
                }

                if (animal instanceof EntityCow) {
                    if (held.getItem() == Items.bucket || held.getItem() == Items.glass_bottle) {
                        event.setCanceled(true);
                    } else if (ToolHelper.isMilker(held)) {
                        if (canProduceProduct(animal)) {
                            int relationship = getRelationship((EntityLivingBase) event.target, player);
                            
                            System.out.println(relationship);
                            
                            int quality = 1 + ((relationship / Short.MAX_VALUE) * 99);
                            Size size = Size.getRandomSize(player.worldObj.rand, quality);
                            ItemStack stack = new ItemStack(HMItems.sized, 1, getDamage(SizeableMeta.MILK, quality, size));

                            if (!player.inventory.addItemStackToInventory(stack)) {
                                player.dropPlayerItemWithRandomChoice(stack, false);
                            }

                            setProducedProduct(animal);
                        }

                        return;
                    }
                }
            } else if (held == null && animal instanceof EntityChicken) {
                animal.ridingEntity = player;
                player.riddenByEntity = animal;
            }
        }
    }
}
