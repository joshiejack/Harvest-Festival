package joshie.harvestmoon.core.handlers.events;

import static joshie.harvestmoon.core.helpers.CropHelper.addFarmland;
import joshie.harvestmoon.init.HMConfiguration;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ToolEvents {
    @SubscribeEvent
    public void onUseHoe(UseHoeEvent event) {
        if (HMConfiguration.vanilla.HOES_ARE_USELESS) {
            event.setCanceled(true);
            return;
        }

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
}
