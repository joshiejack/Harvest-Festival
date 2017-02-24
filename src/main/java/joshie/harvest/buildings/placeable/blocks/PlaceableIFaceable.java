package joshie.harvest.buildings.placeable.blocks;

import com.google.gson.annotations.Expose;
import joshie.harvest.core.util.interfaces.IFaceable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


@SuppressWarnings("WeakerAcess")
public class PlaceableIFaceable extends PlaceableBlock {
    @Expose
    private EnumFacing facing;

    public PlaceableIFaceable() {}
    public PlaceableIFaceable(EnumFacing facing, IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
        this.facing = facing;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IFaceable) {
            EnumFacing orientation = rotation.rotate(facing);
            ((IFaceable) tile).setFacing(orientation);
        }
    }
}