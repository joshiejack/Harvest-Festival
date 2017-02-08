package joshie.harvest.crops.handlers.growth;

import joshie.harvest.api.trees.GrowthHandlerTree;
import joshie.harvest.core.util.ResourceLoader;
import joshie.harvest.core.util.HFTemplate;
import joshie.harvest.crops.HFCrops;
import joshie.harvest.crops.block.BlockFruit.Fruit;
import joshie.harvest.crops.block.BlockHFCrops.CropType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static joshie.harvest.buildings.HFBuildings.getGson;
import static joshie.harvest.core.lib.HFModInfo.MODID;

@SuppressWarnings("WeakerAccess")
public abstract class GrowthHandlerHFTree extends GrowthHandlerTree {
    private final HFTemplate template;
    private final Fruit fruit;

    public GrowthHandlerHFTree(Fruit fruit) {
        this.fruit = fruit;
        this.template = getGson().fromJson(ResourceLoader.getJSONResource(new ResourceLocation(MODID, fruit.getName()), "trees"), HFTemplate.class);;
    }

    @Override
    protected void growJuvenile(World world, BlockPos pos) {
        world.setBlockState(pos.up(), HFCrops.CROPS.getStateFromEnum(CropType.FRESH_DOUBLE));
    }

    @Override
    protected void growTree(World world, BlockPos pos) {
        Rotation rotation = Rotation.values()[world.rand.nextInt(Rotation.values().length)];
        template.placeBlocks(world, getAdjustedPositionBasedOnRotation(pos, rotation), rotation, null);
    }

    protected abstract BlockPos getAdjustedPositionBasedOnRotation(BlockPos pos, Rotation rotation);

    protected abstract boolean isLeaves(IBlockState state);

    @Override
    protected void growFruit(World world, BlockPos thePos) {
        int attempts = 0;
        while (true) {
            BlockPos pos = thePos.add(world.rand.nextInt(16) - 8, world.rand.nextInt(8), world.rand.nextInt(16) - 8);
            if (world.isAirBlock(pos)) {
                IBlockState above = world.getBlockState(pos.up());
                if (isLeaves(above)) {
                    world.setBlockState(pos, HFCrops.FRUITS.getStateFromEnum(fruit));
                    break;
                }
            }

            if (attempts == 1000) break;
            attempts++;
        }
    }
}
