package joshie.harvest.core.handlers;

import joshie.harvest.core.util.HFEvents;
import joshie.harvest.crops.HFCrops;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static joshie.harvest.animals.HFAnimals.DISABLE_SPAWN_CHICKEN;
import static joshie.harvest.crops.HFCrops.*;
import static net.minecraft.init.Items.*;
import static net.minecraft.init.Items.CARROT;
import static net.minecraft.init.Items.POTATO;

public class DisableHandler {
    /* Disables vanilla seeds from being able to be planted **/
    @HFEvents
    public static class DisableVanillaSeeds {
        public static final Set<Item> BLACKLIST = new HashSet<>();

        public static boolean register() {
            if (DISABLE_VANILLA_SEEDS) {
                BLACKLIST.add(MELON_SEEDS);
                BLACKLIST.add(PUMPKIN_SEEDS);
                BLACKLIST.add(BEETROOT_SEEDS);
                BLACKLIST.add(WHEAT_SEEDS);
                BLACKLIST.add(CARROT);
                BLACKLIST.add(POTATO);
                return true;
            } else return false;
        }

        @SubscribeEvent
        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            ItemStack held = event.getItemStack();
            if (held != null) {
                if (BLACKLIST.contains(held.getItem())) {
                    event.setUseItem(Result.DENY);
                }
            }
        }
    }

    /* Disables vanilla eggs from spawning chickens **/
    @HFEvents
    public static class DisableEggSpawning {
        public static boolean register() { return DISABLE_SPAWN_CHICKEN; }

        @SubscribeEvent
        public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            ItemStack held = event.getItemStack();
            if (held != null) {
                if (held.getItem() == EGG) {
                    event.setResult(Result.DENY);
                }
            }
        }
    }

    /* Disables the use of vanilla hoes **/
    @HFEvents
    public static class DisableHoes {
        public static boolean register() { return DISABLE_VANILLA_HOE; }

        @SubscribeEvent
        public void onUseHoe(UseHoeEvent event) {
            if (DISABLE_VANILLA_HOE) {
                event.setCanceled(true);
            }
        }
    }

    @HFEvents
    public static class BreakBlock {
        public static boolean register() { return VANILLA_CROP_UNSELLABLE; }

        @SubscribeEvent
        public void onHarvested(BlockEvent.HarvestDropsEvent event) {
            Block block = event.getState().getBlock();
            if (block != HFCrops.CROPS && block instanceof BlockCrops) {
                for (ItemStack stack: event.getDrops()) {
                    if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setBoolean("Unsellable", true);
                }
            }
        }
    }

    @HFEvents
    public static class DisableFarming {
        public static boolean register() { return DISABLE_VILLAGER_FARMING; }

        @SubscribeEvent
        @SuppressWarnings("deprecation")
        public void onJoinWorld(EntityJoinWorldEvent event) {
            if (event.getEntity() instanceof EntityVillager) {
                EntityVillager villager = (EntityVillager) event.getEntity();
                if (villager.getProfession() == 0) {
                    Iterator<EntityAITaskEntry> iterator = villager.tasks.taskEntries.iterator();
                    while (iterator.hasNext()) {
                        EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
                        EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;
                        if (entityaibase instanceof EntityAIHarvestFarmland) {
                            iterator.remove();
                            return;
                        }
                    }
                }
            }
        }
    }
}
