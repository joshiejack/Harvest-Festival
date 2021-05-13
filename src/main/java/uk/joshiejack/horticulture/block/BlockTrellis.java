package uk.joshiejack.horticulture.block;

import uk.joshiejack.horticulture.item.ItemCrop;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static uk.joshiejack.horticulture.Horticulture.MODID;

public abstract class BlockTrellis extends BlockCrop {
    private static final AxisAlignedBB COLLISION = new AxisAlignedBB(0.2D, 0.0D, 0.2D, 0.8D, 1.5D, 0.8D);
    protected BlockTrellis NS;
    protected BlockTrellis EW;

    public BlockTrellis(ResourceLocation resource, ItemCrop.Crops crop, int num, int regrow) {
        super(resource, crop, num, regrow);
        setHardness(0.6F);
        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return COLLISION;
    }

    @Override
    protected ItemStack getSeedStack() {
        return ItemStack.EMPTY;
    }

    public BlockTrellis getEW() {
        return EW;
    }

    public BlockTrellis getNS() {
        return NS;
    }

    public static class BlockTrellisNS extends BlockTrellis {
        public BlockTrellisNS(ItemCrop.Crops crop, int num, int regrow) {
            super(new ResourceLocation(MODID, crop.getName() + "_ns"), crop, num, regrow);
        }
    }

    public static class BlockTrellisEW extends BlockTrellis {
        public BlockTrellisEW(ItemCrop.Crops crop, int num, int regrow) {
            super(new ResourceLocation(MODID, crop.getName() + "_ew"), crop, num, regrow);
        }

        public BlockTrellis setDirectionalBlock(BlockTrellis block) {
            block.EW = this;
            block.NS = block;
            this.EW = this;
            this.NS = block;
            return this;
        }
    }
}
