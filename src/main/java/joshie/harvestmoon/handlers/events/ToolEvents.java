package joshie.harvestmoon.handlers.events;

import joshie.harvestmoon.items.ItemSickle;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToolEvents {
    @SubscribeEvent
    public void onBlockStartBreak(BreakSpeed event) {
        if (event.entityPlayer.getCurrentEquippedItem() != null) {
            Item item = event.entityPlayer.getCurrentEquippedItem().getItem();
            if (item instanceof ItemSickle) {
                ((ItemSickle) item).onBreakSpeedUpdate(event.entityPlayer, event.entityPlayer.getCurrentEquippedItem(), event.entityPlayer.worldObj, event.x, event.y, event.z);
            }
        }
    }
}
