package joshie.harvest.core.achievements;

import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.entity.EntityDarkChick;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

@HFEvents
public class AchievementEvents {
    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        ItemStack stack = event.pickedUp.getEntityItem();
        EntityPlayer player = event.player;
        if (InventoryHelper.ITEM_STACK.matches(stack, HFCrops.TURNIP.getCropStack(1))) {
            player.addStat(HFAchievements.harvest);
        } else if (InventoryHelper.ITEM_STACK.matches(stack, HFMining.MATERIALS.getStackFromEnum(Material.MYSTRIL))) {
            player.addStat(HFAchievements.mystril);
        }
    }

    @SubscribeEvent
    public void onDeath(PlayerChangedDimensionEvent event) {
        if (event.toDim == HFMining.MINING_ID) {
            event.player.addStat(HFAchievements.theMine);
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer && event.getSource().getEntity() instanceof EntityDarkChick) {
            ((EntityPlayer)event.getEntity()).addStat(HFAchievements.deathByChick);
        }
    }
}
