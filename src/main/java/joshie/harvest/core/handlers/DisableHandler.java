package joshie.harvest.core.handlers;

import joshie.harvest.core.util.HFEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.animals.HFAnimals.DISABLE_SPAWN_CHICKEN;
import static joshie.harvest.crops.HFCrops.DISABLE_VANILLA_HOE;
import static joshie.harvest.crops.HFCrops.DISABLE_VANILLA_SEEDS;
import static net.minecraft.init.Items.*;

public class DisableHandler {
    /* Disables vanilla seeds from being able to be planted **/
    @HFEvents
    public static class DisableVanillaSeeds {
        private static final Set<Item> BLACKLIST = new HashSet<>();

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
}
