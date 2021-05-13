package uk.joshiejack.penguinlib.item.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface PenguinTool {
    void activate(EntityPlayer player, World world, BlockPos pos);
}
