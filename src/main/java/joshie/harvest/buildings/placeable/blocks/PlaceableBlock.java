package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumMap;
import java.util.UUID;

public class PlaceableBlock extends Placeable {
    protected transient EnumMap<Direction, IBlockState> states = new EnumMap<Direction, IBlockState>(Direction.class);
    protected IBlockState state;

    public PlaceableBlock() {
        super(0, 0, 0);
    }

    public PlaceableBlock(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(offsetX, offsetY, offsetZ);
        state = block.getStateFromMeta(meta);
        for (Direction direction: Direction.values()) {
            states.put(direction, direction.withDirection(state));
        }
    }

    public PlaceableBlock(BlockPos pos, PlaceableBlock block) {
        super(pos.getX(), pos.getY(), pos.getZ());
        state = block.state;
        for (Direction direction: Direction.values()) {
            states.put(direction, direction.withDirection(state));
        }
    }

    @Override
    public PlaceableBlock init() {
        if (states == null) states = new EnumMap<Direction, IBlockState>(Direction.class);
        for (Direction direction: Direction.values()) {
            states.put(direction, direction.withDirection(state));
        }

        return this;
    }

    public Block getBlock() {
        return state.getBlock();
    }

    public IBlockState getTransformedState(Direction direction) {
        return direction.withDirection(state);
    }

    public PlaceableBlock copyWithOffset(BlockPos pos, Direction direction) {
        return new PlaceableBlock(getTransformedPosition(pos, direction), this);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.BUILD;
    }

    public boolean prePlace (UUID owner, World world, BlockPos pos, Direction direction) {
        return world.getBlockState(pos).getBlockHardness(world, pos) != -1.0F;
    }

    @Override
    public final boolean place (UUID owner, World world, BlockPos pos, Direction direction) {
        if (!prePlace(owner, world, pos, direction)) return false;
        IBlockState state = getTransformedState(direction);
        boolean result = world.setBlockState(pos, state, 3);
        if (result) {
            postPlace(owner, world, pos, direction);
        }

        return result;
    }

    public void postPlace (UUID owner, World world, BlockPos pos, Direction direction) {}

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