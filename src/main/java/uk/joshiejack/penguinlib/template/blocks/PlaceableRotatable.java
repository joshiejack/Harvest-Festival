package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.interfaces.Rotatable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@PenguinLoader("rotatable")
public class PlaceableRotatable extends PlaceableBlock {
    private EnumFacing facing;

    @SuppressWarnings("unused")
    public PlaceableRotatable() {}
    public PlaceableRotatable(EnumFacing facing, IBlockState state, BlockPos position) {
        super(state, position);
        this.facing = facing;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof Rotatable) {
            ((Rotatable)tile).setFacing(rotation.rotate(facing));
        }
    }
}