package joshie.harvest.core.block;

import joshie.harvest.core.HFTab;
import joshie.harvest.core.base.block.BlockHFEnum;
import joshie.harvest.core.block.BlockDecorative.DecorativeBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.Locale;

public class BlockDecorative extends BlockHFEnum<BlockDecorative, DecorativeBlock> {
    @SuppressWarnings("WeakerAccess")
    public enum DecorativeBlock implements IStringSerializable {
        COOKING;

        @Override
        public String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }

    public BlockDecorative() {
        super(Material.PISTON, DecorativeBlock.class, HFTab.TOWN);
        setHardness(2.5F);
        setSoundType(SoundType.WOOD);
    }

    @SuppressWarnings("deprecation")
    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }
}
