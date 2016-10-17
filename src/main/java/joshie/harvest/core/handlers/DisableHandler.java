package joshie.harvest.core.handlers;

import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.core.util.holders.HolderRegistrySet;
import net.minecraft.block.Block;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.animals.HFAnimals.DISABLE_SPAWN_CHICKEN;
import static joshie.harvest.crops.HFCrops.*;
import static net.minecraft.init.Items.*;
import static net.minecraft.init.Items.CARROT;
import static net.minecraft.init.Items.POTATO;

public class DisableHandler {
    //Crop Blocks
    public static final Set<Block> CROPS = new HashSet<>();

    /* Disables vanilla seeds from being able to be planted **/
    @HFEvents
    public static class VanillaSeeds {
        public static final HolderRegistrySet BLACKLIST = new HolderRegistrySet();

        public static boolean register() {
            if (DISABLE_VANILLA_SEEDS) {
                BLACKLIST.register(MELON_SEEDS);
                BLACKLIST.register(PUMPKIN_SEEDS);
                BLACKLIST.register(BEETROOT_SEEDS);
                BLACKLIST.register(WHEAT_SEEDS);
                BLACKLIST.register(CARROT);
                BLACKLIST.register(POTATO);
                return true;
            } else return false;
        }

        @SubscribeEvent
        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            ItemStack held = event.getItemStack();
            if (held != null) {
                if (BLACKLIST.contains(held)) {
                    event.setUseItem(Result.DENY);
                    return; //Don't continue
                }
            }
        }
    }

    /* Disables vanilla crops from growing **/
    @HFEvents
    public static class VanillaGrowth {
        public static boolean register() { return DISABLE_VANILLA_GROWTH; }

        @SubscribeEvent
        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (CROPS.contains(event.getWorld().getBlockState(event.getPos()).getBlock())) {
                event.setUseItem(Result.DENY);
            }
        }
    }

    /* Disables vanilla crops from dropping **/
    @HFEvents
    public static class VanillaDrops {
        public static boolean register() { return DISABLE_VANILLA_DROPS; }

        @SubscribeEvent
        public void onHarvestBlock(BlockEvent.HarvestDropsEvent event) {
            if (CROPS.contains(event.getState().getBlock())) {
                event.getDrops().clear();
            }
        }
    }

    /* Disables vanilla eggs from spawning chickens **/
    @HFEvents
    public static class EggSpawning {
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
    public static class VanillaHoes {
        public static boolean register() { return DISABLE_VANILLA_HOE; }

        @SubscribeEvent
        public void onUseHoe(UseHoeEvent event) {
            if (DISABLE_VANILLA_HOE && event.getCurrent().getItem() instanceof ItemHoe) {
                event.setCanceled(true);
            }
        }
    }
}
