package uk.joshiejack.horticulture.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

import static uk.joshiejack.horticulture.Horticulture.MODID;

public class BlockLeavesTropical extends BlockLeavesFruit<BlockLeavesTropical.Fruit> {
    public BlockLeavesTropical() {
        super(new ResourceLocation(MODID, "leaves_tropical"), Fruit.class);
    }

    @Override
    public int damageDropped(@Nonnull IBlockState state) {
        return getEnumFromState(state) == Fruit.BANANA ? BlockSapling.Sapling.BANANA.ordinal() : 0;
    }

    @Nullable
    @Override
    protected IBlockState getFruitState(BlockLeavesTropical.Fruit fruit) {
        return fruit == Fruit.BANANA ? HorticultureBlocks.FRUIT.getStateFromEnum(BlockFruit.Fruit.BANANA) : null;
    }

    @Override
    public boolean isValidLocationForFruit(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        for (EnumFacing facing: EnumFacing.HORIZONTALS) {
            BlockPos check = pos.offset(facing);
            if (worldIn.getBlockState(check).getBlock().isWood(worldIn, check)) return true;
        }

        return false;
    }

    public enum Fruit implements IStringSerializable {
        BANANA;

        @Override
        public @Nonnull String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}
