package uk.joshiejack.horticulture.world.gen.feature;

import uk.joshiejack.horticulture.block.BlockSapling;
import uk.joshiejack.horticulture.block.HorticultureBlocks;
import uk.joshiejack.penguinlib.block.base.BlockMultiSapling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class WorldGenFruitTree extends WorldGenerator {
    //private static final Random rotation = new Random();
    private final IBlockState original;
    private final BlockMultiSapling<?> sapling;

    public WorldGenFruitTree(BlockSapling.Sapling sapling)  {
        super(false);
        this.original = HorticultureBlocks.SAPLING.getStateFromEnum(sapling);
        this.sapling = HorticultureBlocks.SAPLING;
    }

    @Override
    public boolean generate(@Nonnull World worldIn, @Nonnull Random rand, @Nonnull BlockPos position) {
        IBlockState soil = worldIn.getBlockState(position.down());
        if (soil.getBlock() == Blocks.GRASS && worldIn.getBlockState(position).getMaterial() != Material.WATER) {
            worldIn.setBlockState(position, original);
            sapling.generateTree(worldIn, position, original, new Random());
            return true;
        }

        return false;
    }
}