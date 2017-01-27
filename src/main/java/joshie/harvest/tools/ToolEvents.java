package joshie.harvest.tools;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.crops.CropHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.tools.HFTools.EXHAUSTION;
import static joshie.harvest.tools.HFTools.FATIGUE;

@HFEvents
@SuppressWarnings("unused")
public class ToolEvents {
    @HFEvents
    @SuppressWarnings("unused")
    public static class AttackFainting {
        public static boolean register() { return HFTools.ATTACK_FAINTING; }

        @SubscribeEvent
        public void onAttackEntity(AttackEntityEvent event){
            ToolHelper.consumeHunger(event.getEntityPlayer(), 0F);
        }
    }

    @HFEvents
    @SuppressWarnings("unused")
    public static class BreakFainting {
        public static boolean register() { return HFTools.BLOCK_FAINTING; }

        @SubscribeEvent
        public void onHarvestBlock(HarvestDropsEvent event){
            ToolHelper.consumeHunger(event.getHarvester(), 0F);
        }
    }

    @HFEvents
    @SuppressWarnings("unused")
    public static class CursedTools {
        @SubscribeEvent
        public void openContainer(PlayerTickEvent event) {
            if (!event.player.capabilities.isCreativeMode && event.player.worldObj.getTotalWorldTime() % 200 == 0) {
                int level = 0;
                Set<Item> added = new HashSet<>();
                for (ItemStack stack : event.player.inventory.mainInventory) {
                    if (stack != null && !added.contains(stack.getItem()) && stack.getItem() instanceof ItemTool) {
                        if (((ItemTool) stack.getItem()).getTier(stack) == ToolTier.CURSED) {
                            added.add(stack.getItem());
                            level++;
                            if (level >= 3) {
                                break;
                            }
                        }
                    }
                }

                if (level > 0) {
                    event.player.addPotionEffect(new PotionEffect(HFTools.CURSED, 200, Math.min(3, level) - 1, true, false));
                }
            }
        }
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.LeftClickBlock event) {
        EntityPlayer player =  event.getEntityPlayer();
        if (player.motionY <= -0.1F) {
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == HFTools.HAMMER) {
                if (CropHelper.getWateringHandler(event.getWorld(), event.getPos(), event.getWorld().getBlockState(event.getPos())) != null) {
                    HFTools.HAMMER.smashBlock(event.getWorld(), player, event.getPos(), player.getHeldItemMainhand(), true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase == Phase.END || event.player.worldObj.getTotalWorldTime() %20 == 0) {
            if (event.player.isPotionActive(EXHAUSTION)) {
                if (event.player.worldObj.rand.nextInt(256) == 0) {
                    event.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 300, 0, true, false));
                }
            }
        }
    }

    @SubscribeEvent
    public void onEaten(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            int level = player.getFoodStats().getFoodLevel();
            if (level > 2 && player.isPotionActive(EXHAUSTION)) player.removePotionEffect(EXHAUSTION);
            if (level > 6 && player.isPotionActive(FATIGUE)) player.removePotionEffect(FATIGUE);
        }
    }

    @SubscribeEvent
    public void onItemPickup(ItemPickupEvent event) {
        ItemStack stack = event.pickedUp.getEntityItem();
        if (stack.getItem() instanceof ItemTool) {
            HFTrackers.getPlayerTrackerFromPlayer(event.player).getTracking().addAsObtained(stack);
        }
    }

    //Axe
    @SubscribeEvent
    public void onBlockDrops(HarvestDropsEvent event){
        if (event.getHarvester() != null) {
            EntityPlayer player = event.getHarvester();
            if (!player.isSneaking() && player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == HFTools.AXE && HFTools.AXE.findTree(player.worldObj, event.getPos())) {
                if (!HFTools.AXE.hasReachedLimit(player.getHeldItemMainhand())) {
                    if (event.getState().getBlock().isWood(event.getWorld(), event.getPos())) {
                        //event.getDrops().clear();
                    }
                }
            }
        }
    }
}
