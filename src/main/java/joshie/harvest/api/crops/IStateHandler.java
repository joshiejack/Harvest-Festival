package joshie.harvest.api.crops;

import com.google.common.collect.ImmutableList;
import joshie.harvest.api.calendar.Season;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

import static net.minecraft.block.Block.NULL_AABB;

public interface IStateHandler {
    /** Returns a list of all valid states **/
    ImmutableList<IBlockState> getValidStates();

    /** Returns the bounding box for this crop
     * @param world     the world
     * @param pos       the position
     * @param section   the plant section
     * @param stage     the stage
     * @param withered  if the crop is withered
     * @return the collision box */
    AxisAlignedBB getBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered);

    /** Returns the collision box for this crop
     * @param world     the world
     * @param pos       the position
     * @param section   the plant section
     * @param stage     the stage
     * @param withered  if the crop is withered
     * @return the collision box */
    default AxisAlignedBB getCollisionBoundingBox(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered) { return NULL_AABB; }

    /** Called to get the colur for this crop
     * @param world       the world object
     * @param pos         the position of the crop
     * @param renderState the state this crop is rendering as
     * @param season      the season, may be null
     * @param crop        the crop
     * @param withered    if the crop is withered
     * @return the colour, -1 if none */
    default int getColor(IBlockAccess world, BlockPos pos, IBlockState renderState, @Nullable Season season, Crop crop, boolean withered) {
        return withered ? 0xA64DFF : -1;
    }

    /** Returns the current state for this crop
     * @param world     the world
     * @param pos       the position
     * @param section   the plant section
     * @param stage     the stage
     * @param withered  if the crop is withered
     * @return the collision box */
    IBlockState getState(IBlockAccess world, BlockPos pos, PlantSection section, int stage, boolean withered);

    enum PlantSection {
        TOP, BOTTOM
    }
}