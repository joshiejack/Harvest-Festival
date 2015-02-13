package joshie.harvestmoon.handlers.events;

import static joshie.harvestmoon.helpers.CropHelper.addFarmland;
import joshie.harvestmoon.items.ItemSickle;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToolEvents {
    @SubscribeEvent
    public void onUseHoe(UseHoeEvent event) {
        World world = event.world;
        int x = event.x;
        int y = event.y;
        int z = event.z;

        Block block = world.getBlock(x, y, z);
        if (world.getBlock(x, y + 1, z).isAir(world, x, y + 1, z) && (block == Blocks.grass || block == Blocks.dirt)) {
            if (!world.isRemote) {
                addFarmland(world, x, y, z);
            }
        }
    }

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
