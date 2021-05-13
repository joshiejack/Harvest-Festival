package uk.joshiejack.settlements.util;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.penguinlib.template.Placeable;
import uk.joshiejack.penguinlib.template.Template;
import uk.joshiejack.penguinlib.template.blocks.PlaceableBlock;

import javax.annotation.Nonnull;

public class BuildingMaterialsCalculator {
    private static final Object2DoubleMap<Block> wood = new Object2DoubleOpenHashMap<>();
    private static final Object2DoubleMap<Block> stone = new Object2DoubleOpenHashMap<>();
    private static final Object2DoubleMap<Block> glass = new Object2DoubleOpenHashMap<>();
    static {
        wood.put(Blocks.LOG, 1);
        wood.put(Blocks.LOG2, 1);
        wood.put(Blocks.PLANKS, 0.25);
        wood.put(Blocks.BOOKSHELF, 1.5);
        wood.put(Blocks.ACACIA_STAIRS, 0.4);
        wood.put(Blocks.BIRCH_STAIRS, 0.4);
        wood.put(Blocks.DARK_OAK_STAIRS, 0.4);
        wood.put(Blocks.JUNGLE_STAIRS, 0.4);
        wood.put(Blocks.SPRUCE_STAIRS, 0.4);
        wood.put(Blocks.WOODEN_SLAB, 0.5);
        wood.put(Blocks.DOUBLE_WOODEN_SLAB, 1);
        wood.put(Blocks.OAK_STAIRS, 0.4);
        wood.put(Blocks.OAK_DOOR, 1);
        wood.put(Blocks.ACACIA_DOOR, 1);
        wood.put(Blocks.DARK_OAK_DOOR, 1);
        wood.put(Blocks.SPRUCE_DOOR, 1);
        wood.put(Blocks.BIRCH_DOOR, 1);
        wood.put(Blocks.JUNGLE_DOOR, 1);
        wood.put(Blocks.TORCH, 1);
        wood.put(Blocks.TRAPDOOR, 0.25);
        wood.put(Blocks.WOODEN_BUTTON, 0.25);
        wood.put(Blocks.ACACIA_FENCE, 0.7);
        wood.put(Blocks.BIRCH_FENCE, 0.7);
        wood.put(Blocks.DARK_OAK_FENCE, 0.7);
        wood.put(Blocks.JUNGLE_FENCE, 0.7);
        wood.put(Blocks.OAK_FENCE, 0.7);
        wood.put(Blocks.SPRUCE_FENCE, 0.7);
        wood.put(Blocks.CHEST, 8);
        stone.put(Blocks.STONE_STAIRS, 0.4);
        stone.put(Blocks.STONE_BRICK_STAIRS, 0.4);
        stone.put(Blocks.STONE, 1);
        stone.put(Blocks.MOSSY_COBBLESTONE, 1);
        stone.put(Blocks.COBBLESTONE, 1);
        stone.put(Blocks.STONEBRICK, 1);
        stone.put(Blocks.STONE_BUTTON, 0.25);
        stone.put(Blocks.STONE_SLAB, 0.5);
        stone.put(Blocks.DOUBLE_STONE_SLAB, 1);
        stone.put(Blocks.DOUBLE_STONE_SLAB2, 1);
        stone.put(Blocks.STONE_SLAB2, 0.5);
        glass.put(Blocks.GLASS, 1);
        glass.put(Blocks.STAINED_GLASS, 1);
        glass.put(Blocks.STAINED_GLASS_PANE, 0.333);
        glass.put(Blocks.GLASS_PANE, 0.333);
    }

    public static void log(ResourceLocation building, @Nonnull Template template) {
        //Fill out these maps
        double woodCount = 0;
        double stoneCount = 0;
        double glassCount = 0;
        for (Placeable placeable : template.getComponents()) {
            if (placeable instanceof PlaceableBlock) {
                IBlockState state = ((PlaceableBlock) placeable).getState();
                if (wood.containsKey(state.getBlock())) woodCount += wood.get(state.getBlock());
                else if (stone.containsKey(state.getBlock())) stoneCount += stone.get(state.getBlock());
                else if (glass.containsKey(state.getBlock())) glassCount += glass.get(state.getBlock());
            }
        }

        Settlements.logger.info(String.format("Calculating building materials for... " + building + "... found roughly... %s wood / %s stone / %s glass",
                (int) Math.ceil(woodCount * 0.15D), (int) Math.floor(stoneCount * 0.35D), (int) Math.floor(glassCount * 0.6D)));
    }
}
