package joshie.harvest.core.handlers;

import joshie.harvest.core.config.Animals;
import joshie.harvest.core.config.Crops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

import static net.minecraft.init.Items.*;

public class DisableHandler {
    private static final Set<Item> BLACKLIST = new HashSet<Item>();

    static {
        BLACKLIST.add(MELON_SEEDS);
        BLACKLIST.add(PUMPKIN_SEEDS);
        BLACKLIST.add(BEETROOT_SEEDS);
        BLACKLIST.add(WHEAT_SEEDS);
        BLACKLIST.add(CARROT);
        BLACKLIST.add(POTATO);
    }

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (Crops.disableVanillaSeeds) {
            ItemStack held = event.getItemStack();
            if (held != null) {
                if (BLACKLIST.contains(held.getItem())) {
                    event.setUseItem(Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (Animals.DISABLE_SPAWN_CHICKEN) {
            ItemStack held = event.getItemStack();
            if (held != null) {
                if (held.getItem() == EGG) {
                    event.setResult(Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public void onUseHoe(UseHoeEvent event) {
        if (Crops.disableVanillaHoe) {
            event.setCanceled(true);
        }
    }
}
