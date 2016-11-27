package joshie.harvest.api.trees;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.crops.GrowthHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrowthHandlerTree extends GrowthHandler<Tree> {
    /** Trees are returning true here so that they will never die **/
    @Override
    public boolean canGrow(World world, BlockPos pos, Tree tree) {
        return true;
    }

    @Override
    public int grow(World world, BlockPos pos, Tree tree, int stage) {
        if(HFApi.calendar.getSeasonAtCoordinates(world, pos) == Season.WINTER) return stage; //Tree won't grow in winter
        //If we have reached the stage where we are a real tree
        //Then set the blocks in the world
        if (stage == tree.getStages() - 1) {
            growTree(world, pos);
        }

        //If we he haven't reached maturity, continue growing
        if (stage < tree.getStagesToMaturity()) {
            stage++;
        }

        //If our tree is mature, and we're in season, then we should grow fruit for the tree
        if (stage >= tree.getStagesToMaturity() && isCorrectSeason(world, pos, tree)) {
            growFruit(world, pos);
            return tree.getRegrowStage(); //Reset the tree to the less than maturity
        }

        return stage;
    }

    @Override
    public boolean canPlantSeedAt(World world, BlockPos pos, IBlockState soil, Tree tree, BlockPos original) {
        return !(!world.isAirBlock(pos.east()) || !world.isAirBlock(pos.west()) || !world.isAirBlock(pos.north()) || !world.isAirBlock(pos.south()))
                && !(!world.isAirBlock(pos.north().east()) || !world.isAirBlock(pos.north().west()) || !world.isAirBlock(pos.north().west()) || !world.isAirBlock(pos.south().east()))
                    && pos.equals(original) && super.canPlantSeedAt(world, pos, soil, tree, original);
    }

    @Override
    public boolean canHarvest(Tree tree, int stage) {
        return false;
    }

    /** Grow the tree, setting the blocks
     *  @param world    the world
     *  @param pos      the position of the trunk block**/
    protected void growTree(World world, BlockPos pos) {}

    /** Grow the fruit, setting the blocks
     *  @param world    the world
     *  @param pos      the position of the trunk block**/
    protected void growFruit(World world, BlockPos pos) {}
}