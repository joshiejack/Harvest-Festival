package joshie.harvest.calendar;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.calendar.*;
import joshie.harvest.calendar.data.SeasonData;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.town.TownHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.HashMap;

@HFApiImplementation
public class CalendarAPI implements CalendarManager {
    public static final CalendarAPI INSTANCE = new CalendarAPI();
    private final EnumMap<Season, SeasonData> data = new EnumMap<>(Season.class);
    private final TIntObjectMap<SeasonProvider> providers = new TIntObjectHashMap<>();
    private final HashMap<Pair<Integer, Season>, Festival> festivals = new HashMap<>();

    //Winter: Leaves 0xFFFFFF | Grass 0xFFFFFF
    //Autumn: Leaves 0xFF9900 | Grass 0xB25900
    //Summer: Leaves NONE     | Grass 0x4A9C2E
    //Spring: Leaves 0x80B76C     | Grass NONE
    private CalendarAPI() {
        data.put(Season.SPRING, new SeasonData(Season.SPRING, 0x87CEFA, 0, 0x80B76C, 0.65D, 0.0F, 80D, 100D, 0D, 0D, 0D));
        data.put(Season.SUMMER, new SeasonData(Season.SUMMER, 7972863, 0x4A9C2E, 0, 0.4D, 0.0011F, 98D, 99D, 100D, 0D, 0D));
        data.put(Season.AUTUMN, new SeasonData(Season.AUTUMN, 0x8CBED6, 0xB25900, 0xFF9900, 1.08D, -0.07F, 50D, 100D, 0D, 0D, 0D));
        data.put(Season.WINTER, new SeasonData(Season.WINTER, 0xFFFFFF, 0xFFFFFF, 0xCCCCCC, 1.56D, -0.1375F, 45D, 0D, 0D, 90D, 100D));
    }

    @Override
    public void registerSeasonProvider(int dimension, SeasonProvider provider) {
        providers.put(dimension, provider);
    }

    @Override
    public SeasonProvider getSeasonProvider(@Nonnull World world) {
        SeasonProvider provider = providers.get(world.provider.getDimension());
        return provider != null ? provider : HFTrackers.getCalendar(world);
    }

    @Override
    public Season getSeasonAtCoordinates(World world, BlockPos pos) {
        return getSeasonProvider(world).getSeasonAtPos(world, pos);
    }

    @Override
    public Weather getWeather(World world) {
        return HFTrackers.getCalendar(world).getTodaysWeather();
    }

    @Override
    public void registerFestival(Festival festival, int day, Season season) {
        festivals.put(Pair.of(day, season), festival);
    }

    @Override
    public Festival getFestival(World world, BlockPos pos) {
        return TownHelper.getClosestTownToBlockPos(world, pos).getFestival();
    }

    @Override
    public CalendarDate getDate(World world) {
        return HFTrackers.getCalendar(world).getDate();
    }

    @Nonnull
    public SeasonData getDataForSeason(Season season) {
        SeasonData seasonData = data.get(season);
        return seasonData != null ? seasonData : data.get(Season.SPRING);
    }
}