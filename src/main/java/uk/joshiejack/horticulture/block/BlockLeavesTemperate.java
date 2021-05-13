package uk.joshiejack.horticulture.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Locale;

import static uk.joshiejack.horticulture.Horticulture.MODID;

public class BlockLeavesTemperate extends BlockLeavesFruit<BlockLeavesTemperate.Fruit> {
    public BlockLeavesTemperate() {
        super(new ResourceLocation(MODID, "leaves_temperate"), Fruit.class);
    }

    @Override
    public int damageDropped(@Nonnull IBlockState state) {
        switch (getEnumFromState(state)) {
            case APPLE:
                return BlockSapling.Sapling.APPLE.ordinal();
            case ORANGE:
                return BlockSapling.Sapling.ORANGE.ordinal();
            case PEACH:
                return BlockSapling.Sapling.PEACH.ordinal();
        }

        return 0;
    }

    @Nullable
    @Override
    protected IBlockState getFruitState(Fruit fruit) {
        switch (fruit) {
            case APPLE:
                return HorticultureBlocks.FRUIT.getStateFromEnum(BlockFruit.Fruit.APPLE);
            case ORANGE:
                return HorticultureBlocks.FRUIT.getStateFromEnum(BlockFruit.Fruit.ORANGE);
            case PEACH:
                return HorticultureBlocks.FRUIT.getStateFromEnum(BlockFruit.Fruit.PEACH);
        }

        return null;
    }

    @Override
    protected boolean isValidLocationForFruit(World world, BlockPos pos) {
        for (BlockPos target: BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {
            if (world.getBlockState(target).getBlock() == HorticultureBlocks.FRUIT) return false;
        }

        return true;
    }

    public enum Fruit implements IStringSerializable {
        APPLE, ORANGE, PEACH;

        @Override
        public @Nonnull String getName() {
            return toString().toLowerCase(Locale.ENGLISH);
        }
    }
}