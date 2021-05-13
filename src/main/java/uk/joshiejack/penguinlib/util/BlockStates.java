package uk.joshiejack.penguinlib.util;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class BlockStates {
    public static final IBlockState WET_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7);
    public static final IBlockState DRY_SOIL = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 0);
    public static final IBlockState TALL_GRASS = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS);
    public static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    public static final IBlockState DIRT = Blocks.DIRT.getDefaultState();
    public static final IBlockState AIR = Blocks.AIR.getDefaultState();
}
