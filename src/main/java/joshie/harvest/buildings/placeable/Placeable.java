package joshie.harvest.buildings.placeable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public abstract class Placeable {
    protected BlockPos offsetPos;

    public Placeable(BlockPos pos) {
        this.offsetPos = pos;
    }

    public BlockPos getOffsetPos() {
        return offsetPos;
    }

    public int getX() {
        return offsetPos.getX();
    }

    public int getY() {
        return offsetPos.getY();
    }

    public int getZ() {
        return offsetPos.getZ();
    }

    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.BLOCKS;
    }

    public boolean place(UUID owner, World world, BlockPos pos, IBlockState state, boolean n1, boolean n2, boolean swap, PlacementStage stage) {
        if (canPlace(stage)) {
            int y = offsetPos.getY();
            int x = n1 ? -offsetPos.getX() : offsetPos.getX();
            int z = n2 ? -offsetPos.getZ() : offsetPos.getZ();
            if (swap) {
                int xClone = x; //Create a copy of X
                x = z; //Set x to z
                z = xClone; //Set z to the old value of x
            }

            return place(owner, world, new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z), state, n1, n2, swap);
        } else return false;
    }

    public abstract boolean place(UUID owner, World world, BlockPos pos, IBlockState state, boolean n1, boolean n2, boolean swap);

    public static enum PlacementStage {
        BLOCKS, ENTITIES, TORCHES, NPC, FINISHED;
    }
}