package joshie.harvest.api.calendar;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public interface CalendarManager {
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

    /** Returns the date at this location, you should only use this,
     *  when you want to know the location based season, for example when greenhouses are added
     *  then this will return the season of the greenhouse, use getDate(world).getSeason() for most purposes
     *  @param world the world to check
     *  @param pos the block position to check
     *  @return the season at these coordinates**/
    @Nullable
    Season getSeasonAtCoordinates(World world, BlockPos pos);

    /** Return todays weather
     * @param world a world object **/
    Weather getWeather(World world);

    /** Register a holiday
     *  @param festival the holiday data
     *  @param day the date this holiday occurs (will be fit in to a 30 day calendar)
     *  @param season the season of this holiday**/
    void registerFestival(Festival festival, int day, Season season);

    /** Fetch the current festival
     *  @param world    the world object
     *  @param pos      the location we're checking for the holiday
     *  @return the current holiday**/
    @Nonnull
    Festival getFestival(World world, BlockPos pos);
}