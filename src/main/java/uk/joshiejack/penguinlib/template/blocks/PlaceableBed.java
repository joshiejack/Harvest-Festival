package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@PenguinLoader("bed")
public class PlaceableBed extends PlaceableBlock {
    private int color;

    @SuppressWarnings("unused")
    public PlaceableBed() {}
    public PlaceableBed(EnumDyeColor color, IBlockState state, BlockPos position) {
        super(state, position);
        this.color = color.getMetadata();
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEntityBed) {
            ((TileEntityBed)tile).setColor(EnumDyeColor.byMetadata(color));
        }
    }
}