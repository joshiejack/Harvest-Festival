package uk.joshiejack.penguinlib.block;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.block.base.BlockSingular;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static uk.joshiejack.penguinlib.PenguinLib.MOD_ID;

public class BlockInternalAir extends BlockSingular {
    public BlockInternalAir() {
        super(new ResourceLocation(MOD_ID, "internal_air"), Material.GLASS);
        setCreativeTab(PenguinLib.CUSTOM_TAB);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess world,  @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean causesSuffocation(IBlockState state) {
        return false;
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, @Nonnull BlockPos pos) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Nonnull
    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void registerModels(Item item) {}
}
