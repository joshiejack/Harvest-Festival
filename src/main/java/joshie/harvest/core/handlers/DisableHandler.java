package joshie.harvest.core.handlers;

import joshie.harvest.core.util.annotations.HFEvents;
import joshie.harvest.core.util.holders.HolderRegistrySet;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemHoe;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import static joshie.harvest.animals.HFAnimals.DISABLE_SPAWN_CHICKEN;
import static joshie.harvest.crops.HFCrops.*;
import static net.minecraft.init.Items.EGG;

public class DisableHandler {
    //Crop Blocks
    public static final Set<Block> CROPS = new HashSet<>();
    public static final Set<Block> GRASS = new HashSet<>();
    public static final HolderRegistrySet SEEDS_BLACKLIST = new HolderRegistrySet();
    public static final HolderRegistrySet HOE_BLACKLIST = new HolderRegistrySet();


    /* Disables vanilla seeds from being able to be planted **/
    @HFEvents
    @SuppressWarnings("unused")
    public static class VanillaSeeds {
        public static boolean register() { return DISABLE_VANILLA_SEEDS; }

        @SubscribeEvent
        public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
            if (SEEDS_BLACKLIST.contains(event.getItemStack())) {
                event.setUseItem(Result.DENY);
            }
        }
    }

    /* Disables vanilla crops from growing **/
    @HFEvents
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public static class EggSpawning {
        public static boolean register() { return DISABLE_SPAWN_CHICKEN; }

        @SubscribeEvent
        public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
            if (event.getItemStack().getItem() == EGG) {
                event.setResult(Result.DENY);
            }
        }
    }

    /* Disables the use of vanilla hoes **/
    @HFEvents
    @SuppressWarnings("unused")
    public static class VanillaHoes {
        public static boolean register() { return DISABLE_VANILLA_HOE; }

        @SubscribeEvent
        public void onUseHoe(UseHoeEvent event) {
            if (DISABLE_VANILLA_HOE && (event.getCurrent().getItem() instanceof ItemHoe || HOE_BLACKLIST.contains(event.getCurrent()))) {
                event.setCanceled(true);
            }
        }
    }

    /* Disables seeds from dropping from grass **/
    @HFEvents
    @SuppressWarnings("unused")
    public static class SeedDrops {
        public static boolean register() { return DISABLE_VANILLA_WHEAT_SEEDS; }

        @SubscribeEvent
        public void onItemDropping(BlockEvent.HarvestDropsEvent event) {
            if (event.getState().getBlock() == Blocks.TALLGRASS || event.getState().getBlock() == Blocks.DOUBLE_PLANT || GRASS.contains(event.getState().getBlock())) {
                event.getDrops().removeIf(SEEDS_BLACKLIST::contains);
            }
        }
    }
}
