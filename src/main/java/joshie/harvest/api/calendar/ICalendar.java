package joshie.harvest.api.calendar;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public interface ICalendar {
    /** Register a new season provider for your world,
     * @param dimension     the dimension id
     * @param provider      the season provider */
    void registerSeasonProvider(int dimension, SeasonProvider provider);

    /** Fetch the season provider for a specific dimension
     *  @param world    the world object **/
    SeasonProvider getSeasonProvider(@Nonnull World world);

    /** Returns todays date
     * @param world     the world object **/
    CalendarDate getDate(World world);

    /** Returns the date at this location
     *  @param world the world to check
     *  @param pos the block position to check
     *  @return the season at these coordinates**/
    @Nullable
    Season getSeasonAtCoordinates(World world, BlockPos pos);

    /** Return todays weather **/
    Weather getWeather(World world);
}