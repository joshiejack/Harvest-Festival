package uk.joshiejack.penguinlib.template.blocks;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@PenguinLoader("banner")
public class PlaceableBanner extends PlaceableBlock {
    private ItemStack banner;

    @SuppressWarnings("unused")
    public PlaceableBanner() {}
    public PlaceableBanner(ItemStack stack, IBlockState state, BlockPos position) {
        super(state, position);
        this.banner = stack;
    }

    @Override
    public boolean canPlace(ConstructionStage stage) {
        return stage == ConstructionStage.DECORATE;
    }

    @Override
    public void postPlace(World world, BlockPos pos, Rotation rotation) {
        TileEntity tile = world.getTileEntity(pos);
        if (banner != null && tile instanceof TileEntityBanner) {
            ((TileEntityBanner)tile).setItemValues(banner, false);
        }
    }
}