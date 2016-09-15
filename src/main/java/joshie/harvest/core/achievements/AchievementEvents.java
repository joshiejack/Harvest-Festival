package joshie.harvest.core.achievements;

import joshie.harvest.core.helpers.InventoryHelper;
import joshie.harvest.core.util.HFEvents;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.mining.HFMining;
import joshie.harvest.mining.item.ItemMaterial.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
        } else if (InventoryHelper.ITEM_STACK.matches(stack, HFMining.MATERIALS.getStackFromEnum(Material.JUNK))) {
            player.addStat(HFAchievements.junk);
        } else if (InventoryHelper.ITEM_STACK.matches(stack, HFMining.MATERIALS.getStackFromEnum(Material.COPPER))) {
            player.addStat(HFAchievements.copper);
        } else if (InventoryHelper.ITEM_STACK.matches(stack, HFMining.MATERIALS.getStackFromEnum(Material.SILVER))) {
            player.addStat(HFAchievements.summon);
        } else if (InventoryHelper.ITEM_STACK.matches(stack, HFMining.MATERIALS.getStackFromEnum(Material.GOLD))) {
            player.addStat(HFAchievements.gold);
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
}
