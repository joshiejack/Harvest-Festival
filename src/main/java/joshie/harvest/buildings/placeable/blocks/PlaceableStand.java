package joshie.harvest.buildings.placeable.blocks;

import com.google.gson.annotations.Expose;
import joshie.harvest.core.base.tile.TileStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableStand extends PlaceableIFaceable {
    @Expose
    private ItemStack stack;

    @SuppressWarnings("unused")
    public PlaceableStand() {}
    public PlaceableStand(EnumFacing facing, ItemStack stack, IBlockState state, int x, int y, int z) {
        super(facing, state, x, y, z);
        this.stack = stack;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        super.postPlace(world, pos, rotation); //SUPERGIRL!!!!!!!
        TileEntity tile = world.getTileEntity(pos);
        if (stack != null && tile instanceof TileStand) {
            ((TileStand)tile).setContents(stack);
        }
    }
}