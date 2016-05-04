package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableBlock extends Placeable {
    protected IBlockState state;

    public PlaceableBlock() {
        super(BlockPos.ORIGIN);
    }

    public PlaceableBlock(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(new BlockPos(offsetX, offsetY, offsetZ));
        state = block.getStateFromMeta(meta);
    }

    public Block getBlock() {
        return state.getBlock();
    }

    public IBlockState getTransformedState(Mirror mirror, Rotation rotation) {
        return this.state.withMirror(mirror).withRotation(rotation);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.BUILD;
    }

    public boolean prePlace (UUID owner, World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        return true;
    }

    @Override
    public final boolean place (UUID owner, World world, BlockPos pos, Mirror mirror, Rotation rotation) {
        if (!prePlace(owner, world, pos, mirror, rotation)) return false;
        IBlockState state = getTransformedState(mirror, rotation);
        boolean result = world.setBlockState(pos, state, 3);
        if (result) {
            postPlace(owner, world, pos, mirror, rotation);
        }

        return result;
    }

    public void postPlace (UUID owner, World world, BlockPos pos, Mirror mirror, Rotation rotation) {}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if ((!(obj instanceof PlaceableBlock))) return false;
        PlaceableBlock other = (PlaceableBlock) obj;
        if (getX() != other.getX()) return false;
        if (getY() != other.getY()) return false;
        if (getZ() != other.getZ()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + getX();
        result = prime * result + getY();
        result = prime * result + getZ();
        return result;
    }
}