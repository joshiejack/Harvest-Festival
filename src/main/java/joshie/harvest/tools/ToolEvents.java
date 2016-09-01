package joshie.harvest.tools;

import joshie.harvest.api.core.ITiered.ToolTier;
import joshie.harvest.core.base.item.ItemTool;
import joshie.harvest.core.util.HFEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.tools.HFTools.EXHAUSTION;
import static joshie.harvest.tools.HFTools.FATIGUE;

@HFEvents
public class ToolEvents {
    @HFEvents
    public static class AttackFainting {
        public static boolean register() { return HFTools.ATTACK_FAINTING; }

        @SubscribeEvent
        public void onAttackEntity(AttackEntityEvent event){
            ToolHelper.consumeHunger(event.getEntityPlayer(), 0F);
        }
    }

    @HFEvents
    public static class BreakFainting {
        public static boolean register() { return HFTools.BLOCK_FAINTING; }

        @SubscribeEvent
        public void onHarvestBlock(HarvestDropsEvent event){
            ToolHelper.consumeHunger(event.getHarvester(), 0F);
        }
    }

    @HFEvents
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
                        }
                    }
                }

                if (level > 0) {
                    event.player.addPotionEffect(new PotionEffect(HFTools.CURSED, 200, level - 1, true, false));
                }
            }
        }
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent.LeftClickBlock event) {
        EntityPlayer player =  event.getEntityPlayer();
        if (player.motionY <= -0.1F) {
            if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == HFTools.HAMMER) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.FARMLAND) {
                    HFTools.HAMMER.smashBlock(event.getWorld(), player, event.getPos(), player.getHeldItemMainhand(), true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase != Phase.END || event.player.worldObj.getTotalWorldTime() %20 != 0) return;
        else {
            if (event.player.isPotionActive(FATIGUE)) {
                if (event.player.worldObj.rand.nextInt(128) == 0) {
                    event.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 50, 7, true, false));
                }
            } else if (event.player.isPotionActive(EXHAUSTION)) {
                if (event.player.worldObj.rand.nextInt(32) == 0) {
                    event.player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 200, 0, true, false));
                    event.player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 300, 1, true, false));
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
}
