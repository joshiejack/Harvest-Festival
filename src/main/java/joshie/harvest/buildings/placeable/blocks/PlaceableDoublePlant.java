package joshie.harvest.buildings.placeable.blocks;

import joshie.harvest.blocks.BlockPreview.Direction;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class PlaceableDoublePlant extends PlaceableBlock {
    public PlaceableDoublePlant(Block block, int meta, int offsetX, int offsetY, int offsetZ) {
        super(block, meta, offsetX, offsetY, offsetZ);
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(UUID uuid, World world, BlockPos pos, Direction direction) {
        world.setBlockState(pos.up(), state.getBlock().getStateFromMeta(8), 2);
    }
}