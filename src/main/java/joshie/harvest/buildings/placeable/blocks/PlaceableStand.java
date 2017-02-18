package joshie.harvest.buildings.placeable.blocks;

import com.google.gson.annotations.Expose;
import joshie.harvest.core.base.tile.TileStand;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PlaceableStand extends PlaceableBlock {
    @Expose
    private ItemStack stack;

    public PlaceableStand() {}
    public PlaceableStand(ItemStack stack, IBlockState state, int x, int y, int z) {
        super(state, x, y, z);
        this.stack = stack;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (stack != null && tile instanceof TileStand) {
            ((TileStand)tile).setContents(stack);
        }
    }
}