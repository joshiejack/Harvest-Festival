package joshie.harvest.calendar;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import joshie.harvest.api.calendar.*;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.util.HFApiImplementation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.EnumMap;

import static net.minecraft.util.text.TextFormatting.*;

@HFApiImplementation
public class CalendarAPI implements ICalendar {
    public static final CalendarAPI INSTANCE = new CalendarAPI();
    private final EnumMap<Season, SeasonData> data = new EnumMap<>(Season.class);
    private final TIntObjectMap<SeasonProvider> providers = new TIntObjectHashMap<>();

    private CalendarAPI() {
        data.put(Season.SPRING, new SeasonData(Season.SPRING, 0x87CEFA, 0.65D, 0.0F, GREEN, 80D, 100D, 0D, 0D, 0D));
        data.put(Season.SUMMER, new SeasonData(Season.SUMMER, 7972863, 0.4D, 0.0011F, YELLOW, 95D, 75D, 100D, 0D, 0D));
        data.put(Season.AUTUMN, new SeasonData(Season.AUTUMN, 0x8CBED6, 1.08D, -0.07F, GOLD, 50D, 100D, 0D, 0D, 0D));
        data.put(Season.WINTER, new SeasonData(Season.WINTER, 0xFFFFFF, 1.56D, -0.1375F, BLUE, 45D, 0D, 0D, 90D, 100D));
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
    public CalendarDate getDate(World world) {
        return HFTrackers.getCalendar(world).getDate();
    }

    @Nonnull
    public SeasonData getDataForSeason(Season season) {
        SeasonData seasonData = data.get(season);
        return seasonData != null ? seasonData : data.get(Season.SPRING);
    }
}