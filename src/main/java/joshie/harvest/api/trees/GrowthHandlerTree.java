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

        //If we he haven't reached maturity, continue growing
        if (stage < tree.getStages()) {
            stage++;
        }

        //When we reach juvenile, plant the extra block
        if (stage == tree.getStagesToJuvenile()) {
            growJuvenile(world, pos);
        }

        //When we reach maturity, grow the tree
        if (stage == tree.getStagesToMaturity()) {
            growTree(world, pos);
        }


        //If our tree is mature, and we're in season, then we should grow fruit for the tree
        if (stage >= tree.getStages() && isCorrectSeason(world, pos, tree)) {
            growFruit(world, pos); //Grow the fruit, then reset the tree to the stage where we start counting again
            return tree.getRegrowStage(); //Reset the tree to the less than maturity
        }

        return stage;
    }

    @Override
    public boolean canPlantSeedAt(World world, BlockPos pos, IBlockState soil, Tree tree, BlockPos original) {
        return pos.equals(original);
    }

    @Override
    public boolean canHarvest(Tree tree, int stage) {
        return false;
    }

    /** Grow the juvenile, setting the blocks
     *  @param world    the world
     *  @param pos      the position of the trunk block**/
    protected void growJuvenile(World world, BlockPos pos) {}

    /** Grow the tree, setting the blocks
     *  @param world    the world
     *  @param pos      the position of the trunk block**/
    protected void growTree(World world, BlockPos pos) {}

    /** Grow the fruit, setting the blocks
     *  @param world    the world
     *  @param pos      the position of the trunk block**/
    protected void growFruit(World world, BlockPos pos) {}
}