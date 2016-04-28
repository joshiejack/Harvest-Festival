package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.buildings.placeable.Placeable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableBlock extends Placeable {
    protected Block block;
    protected int meta;

    public PlaceableBlock() {
        super(BlockPos.ORIGIN);
    }

    public PlaceableBlock(BlockPos pos) {
        super(pos);
    }

    public PlaceableBlock(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(new BlockPos(offsetX, offsetY, offsetZ));
        this.block = block;
        this.meta = meta;
    }

    public Block getBlock() {
        return block;
    }

    public int getMetaData(boolean n1, boolean n2, boolean swap) {
        return meta;
    }

    public IBlockState getBlockState(boolean n1, boolean n2, boolean swap) {
        return block.getStateFromMeta(meta);
    }

    @Override
    public boolean canPlace(PlacementStage stage) {
        return stage == PlacementStage.BLOCKS;
    }

    @Override
    public boolean place(UUID uuid, World world, BlockPos pos, IBlockState state, boolean n1, boolean n2, boolean swap) {
        if (block == Blocks.AIR && state == Blocks.AIR.getDefaultState()) {
            return false;

        }

        int meta = getMetaData(n1, n2, swap);
        world.setBlockState(pos, block.getDefaultState());
        return world.setBlockState(pos, state.getBlock().getStateFromMeta(meta), 2);
    }

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