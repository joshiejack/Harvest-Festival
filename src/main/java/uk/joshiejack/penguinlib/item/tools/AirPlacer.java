package uk.joshiejack.penguinlib.item.tools;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.item.interfaces.PenguinTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class AirPlacer implements PenguinTool {
    private final boolean remove;

    public AirPlacer(boolean remove) {
        this.remove = remove;
    }

    private boolean isValidBlockToReplace(World world, BlockPos pos) {
        return remove ? world.getBlockState(pos).getBlock() == PenguinLib.AIR : world.getBlockState(pos).getBlock() == Blocks.AIR;
    }

    @Override
    public void activate(EntityPlayer player, World world, BlockPos pos) {
        if (!world.isRemote) {
            Set<BlockPos> positions = new HashSet<>();
            if (isValidBlockToReplace(world, pos.up())) {
                positions.add(pos.up());
                int loop = 24;
                for (int j = 0; j < loop; j++) {
                    Set<BlockPos> temp = new HashSet<>(positions);
                    for (BlockPos coord : temp) {
                        for (EnumFacing theFacing : EnumFacing.VALUES) {
                            BlockPos offset = coord.offset(theFacing);
                            if (isValidBlockToReplace(world, offset)) {
                                positions.add(offset);
                            }
                        }
                    }
                }

                for (BlockPos position : positions) {
                    world.setBlockState(position, remove ? Blocks.AIR.getDefaultState() : PenguinLib.AIR.getDefaultState());
                }
            }
        }
    }
}
