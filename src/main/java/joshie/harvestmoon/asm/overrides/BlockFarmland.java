package joshie.harvestmoon.asm.overrides;

import joshie.harvestmoon.core.helpers.generic.EntityHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class BlockFarmland {
    public static float getPlayerRelativeBlockHardness(Block block, EntityPlayer player, World world, int x, int y, int z) {
        return !EntityHelper.isFakePlayer(player) ? 0.075F : ForgeHooks.blockStrength(block, player, world, x, y, z);
    }
}
