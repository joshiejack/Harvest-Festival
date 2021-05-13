package uk.joshiejack.penguinlib.item.tools;

import uk.joshiejack.penguinlib.item.interfaces.PenguinTool;
import uk.joshiejack.penguinlib.util.helpers.minecraft.ChatHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Coordinates implements PenguinTool {
    private static boolean coordinates1;
    private static boolean coordinates2;
    public static boolean areSet;
    public static BlockPos from;
    public static BlockPos to;

    @Override
    public void activate(EntityPlayer player, World world, BlockPos pos) {
        if (!player.isSneaking()) {
            coordinates1 = true;
            from = pos;
            if (world.isRemote) {
                ChatHelper.displayChat("Set start coordinates to " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
            }
        } else {
            coordinates2 = true;
            to = pos.add(0, 100, 0);
            if (world.isRemote) {
                ChatHelper.displayChat("Set end coordinates to " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
            }
        }

        areSet = coordinates1 && coordinates2;
    }
}
