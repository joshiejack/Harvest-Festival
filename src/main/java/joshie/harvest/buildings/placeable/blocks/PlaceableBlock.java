package joshie.harvest.buildings.placeable.blocks;

import com.google.gson.annotations.Expose;
import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableBlock extends Placeable {
    @Expose
    protected IBlockState state;

    public PlaceableBlock() {}
    public PlaceableBlock(IBlockState state, int x, int y, int z) {
        this.state = state;
        this.pos = new BlockPos(x, y, z);
    }

    public PlaceableBlock(IBlockState state, BlockPos pos) {
        this.state = state;
        this.pos = pos;
    }

    @Override
    public PlaceableBlock init() {
        return this;
    }

    public Block getBlock() {
        return state.getBlock();
    }

    public IBlockState getState() {
        return state;
    }

    public IBlockState getTransformedState(Rotation rotation) {
        return state.withRotation(rotation);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.BUILD;
    }

    private boolean prePlace (World world, BlockPos pos) {
        return world.getBlockState(pos).getBlockHardness(world, pos) != -1.0F;
    }

    @Override
    public final boolean place (World world, BlockPos pos, Rotation rotation, boolean playSound) {
        if (!prePlace(world, pos)) return false;
        IBlockState state = getTransformedState(rotation);
        if (world.getBlockState(pos) == state) return false;
        if(playSound) world.playEvent(null, 2001, pos, Block.getStateId(state));
        boolean result = world.setBlockState(pos, state, 2);
        if (result) {
            postPlace(world, pos, rotation);
        }

        return result;
    }

    @Override
    public void remove(World world, BlockPos pos, Rotation rotation, ConstructionStage stage, IBlockState state) {
        if (canPlace(stage)) {
            BlockPos transformed = getTransformedPosition(pos, rotation);
            world.setBlockState(transformed, state);
        }
    }

    public void postPlace (World world, BlockPos pos, Rotation rotation) {}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if ((!(obj instanceof PlaceableBlock))) return false;
        PlaceableBlock other = (PlaceableBlock) obj;
        return getX() == other.getX() && getY() == other.getY() && (getZ() == other.getZ());
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