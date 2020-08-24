package joshie.harvest.calendar;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.calendar.*;
import joshie.harvest.calendar.data.SeasonData;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.util.annotations.HFApiImplementation;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@HFApiImplementation
public class CalendarAPI implements CalendarManager {
    public static final CalendarAPI INSTANCE = new CalendarAPI();
    private final EnumMap<Season, SeasonData> data = new EnumMap<>(Season.class);
    private final TIntObjectMap<SeasonProvider> providers = new TIntObjectHashMap<>();
    private final Map<CalendarDate, Festival> festivals = new HashMap<>();

    private CalendarAPI() {
        data.put(Season.SPRING, new SeasonData(Season.SPRING, 0x87CEFA, 6000, 20500).setLeavesColor(0x80B76C)
                                    .setWeatherWeight(Weather.SUNNY, 70D).setWeatherWeight(Weather.RAIN, 30D));
        data.put(Season.SUMMER, new SeasonData(Season.SUMMER, 7972863, 5000, 21500).setGrassColor(0x4A9C2E)
                                    .setWeatherWeight(Weather.SUNNY, 95D).setWeatherWeight(Weather.RAIN, 4D).setWeatherWeight(Weather.TYPHOON, 1D));
        data.put(Season.AUTUMN, new SeasonData(Season.AUTUMN, 0x8CBED6, 7000, 19000).setGrassColor(0xB25900).setLeavesColor(0xFF9900)
                                    .setWeatherWeight(Weather.RAIN, 45D).setWeatherWeight(Weather.SUNNY, 55D));
        data.put(Season.WINTER, new SeasonData(Season.WINTER, 0xFFFFFF, 8000, 16500).setGrassColor(0xFFFFFF).setLeavesColor(0xCCCCCC)
                                    .setWeatherWeight(Weather.SUNNY, 49D).setWeatherWeight(Weather.SNOW, 50D).setWeatherWeight(Weather.BLIZZARD, 1D));
    }

    public Festival getFestivalFromDate(TownData data, CalendarDate date) {
        for (Entry<CalendarDate, Festival> entry: festivals.entrySet()) {
            if (CalendarHelper.isDateSame(date, entry.getKey())) {
                Festival festival = entry.getValue();
                if (festival.getRequirement() == null || data.hasBuilding(festival.getRequirement())) {
                    return festival;
                }
            }
        }

        return Festival.NONE;
    }

    public Map<CalendarDate, Festival> getFestivals() {
        return festivals;
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

    //We're offsetting the day in the calendar, so that the letter gets sent the day before the event
    private int getOffsetDay(int originalDay) {
        if (originalDay > 1) return originalDay - 1;
        else return 30;
    }

    //Offsetting the season, so if the day ends up as 30, it will be in the previous season
    private Season getOffsetSeason(int originalDay, Season season) {
        if (originalDay > 1) return season;
        else {
            switch (season) {
                case SPRING:
                    return Season.WINTER;
                case SUMMER:
                    return Season.SPRING;
                case AUTUMN:
                    return Season.SUMMER;
                case WINTER:
                    return Season.AUTUMN;
                default:
                    return season;
            }
        }
    }

    @Override
    public void registerFestival(Festival festival, int originalDay, Season originalSeason) {
        festivals.put(new CalendarDate(getOffsetDay(originalDay), getOffsetSeason(originalDay, originalSeason), 1), festival);
    }

    @Override
    @Nonnull
    public Festival getFestival(World world, BlockPos pos) {
        return TownHelper.getClosestTownToBlockPos(world, pos, false).getFestival();
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