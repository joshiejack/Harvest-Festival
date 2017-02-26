package joshie.harvest.buildings.placeable.blocks;

import com.google.gson.annotations.Expose;
import joshie.harvest.animals.tile.TileTrough;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


@SuppressWarnings("WeakerAcess")
public class PlaceableFillable extends PlaceableBlock {
    @Expose
    private int fill;

    public PlaceableFillable() {}
    public PlaceableFillable(int fill, IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
        this.fill = fill;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileTrough) {
            ((TileTrough) tile).setFilled(fill);
        }
    }
}