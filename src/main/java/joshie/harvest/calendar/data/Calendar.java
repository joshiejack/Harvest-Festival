package joshie.harvest.calendar.data;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.SeasonProvider;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.core.util.HFTracker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static joshie.harvest.api.calendar.Weather.SUNNY;

public abstract class Calendar extends HFTracker implements SeasonProvider {
    public abstract CalendarDate getDate();

    @Override
    public Season getSeasonAtPos(World world, @Nullable BlockPos pos) {
        return getDate().getSeason();
    }

    /* ############# Season Data ################*/
    private SeasonData data;

    public SeasonData getSeasonData() {
        if (data == null) {
            onSeasonChanged();
        }

        return data;
    }

    public void onSeasonChanged() {
        this.data = CalendarAPI.INSTANCE.getDataForSeason(getDate().getSeason());
    }


    /* ############# Weather ################*/
    Weather[] forecast = new Weather[7];
    int rainStrength;
    int stormStrength;

    public Calendar() {
        for (int i = 0; i < 7; i++) {
            forecast[i] = Weather.SUNNY;
        }
    }

    public Weather getTodaysWeather() {
        return forecast[0] != null ? forecast[0] : SUNNY;
    }

    public int getTodaysRainStrength() {
        return rainStrength;
    }

    public int getTodaysStormStrength() {
        return stormStrength;
    }

    void updateWeatherStrength() {
        switch (forecast[0]) {
            case SUNNY:
                rainStrength = 0;
                stormStrength = 0;
                break;
            case RAIN:
            case SNOW:
                rainStrength = 100;
                stormStrength = 0;
                break;
            case TYPHOON:
                rainStrength = 20;
                stormStrength = 100;
                break;
            case BLIZZARD:
                rainStrength = 20;
                stormStrength = 0;
                break;
            default:
                break;
        }
    }
}