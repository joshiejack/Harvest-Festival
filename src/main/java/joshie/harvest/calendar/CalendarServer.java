package joshie.harvest.calendar;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.ICalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.helpers.NBTHelper.ISaveable;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.core.network.PacketSyncForecast;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

import static joshie.harvest.api.calendar.Season.SPRING;
import static joshie.harvest.core.config.Calendar.DAYS_PER_SEASON;

public class CalendarServer extends Calendar implements ISaveable {
    private final ICalendarDate DATE = HFApi.calendar.newDate(0, SPRING, 1);
    private static final Random rand = new Random();

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        recalculate();
    }

    @Override
    public ICalendarDate getDate() {
        return DATE;
    }

    @Override
    public void setTodaysWeather(Weather weather) {
        forecast[0] = weather;
        updateForecast();
    }

    public static boolean isWeatherDisabled(Weather weather) {
        switch (weather) {
            case SUNNY:
                return joshie.harvest.core.config.Calendar.ENABLE_SUNNY;
            case RAIN:
                return joshie.harvest.core.config.Calendar.ENABLE_RAIN;
            case TYPHOON:
                return joshie.harvest.core.config.Calendar.ENABLE_TYPHOON;
            case SNOW:
                return joshie.harvest.core.config.Calendar.ENABLE_SNOW;
            case BLIZZARD:
                return joshie.harvest.core.config.Calendar.ENABLE_BLIZZARD;
            default:
                return false;
        }
    }

    public Weather getRandomWeather(int day, Season season) {
        if (day > DAYS_PER_SEASON) {
            season = getNextSeason(season);
        }

        ISeasonData data = HFApi.calendar.getDataForSeason(season);
        for (Weather weather : Weather.values()) {
            if (isWeatherDisabled(weather)) continue;
            double chance = data.getWeatherChance(weather);
            if (rand.nextDouble() * 100D < chance) {
                return weather;
            }
        }

        return Weather.SUNNY;
    }

    @Override
    public float getTodaysRainStrength() {
        return rainStrength;
    }

    @Override
    public void updateForecast() {
        //If they're null set them
        for (int i = 0; i < 7; i++) {
            if (forecast[i] == null) {
                forecast[i] = getRandomWeather(getDate().getDay() + i, getDate().getSeason());
            }
        }

        updateWeatherStrength();
        PacketHandler.sendToEveryone(new PacketSyncForecast(getDimension(), forecast));
    }

    @Override
    public void recalculate() {
        CalendarHelper.setDate(getWorld(), DATE);
    }

    @Override
    public void recalculateAndUpdate() {
        CalendarHelper.setDate(getWorld(), DATE);
        PacketHandler.sendToEveryone(new PacketSetCalendar(getDimension(), getDate())); //Sync the new date
    }

    @Override
    public void newDay() {
        recalculateAndUpdate(); //Update the date
        /** Setup the forecast for the next 7 days **/
        Weather[] newForecast = new Weather[7];

        //Copy over the old forecast
        /*for (int i = 1; i <= 6; i++) {
            newForecast[i - 1] = forecast[i];
        }*/

        System.arraycopy(forecast, 1, newForecast, 0, 6);

        forecast = newForecast;
        updateForecast();
    }

    private Season getNextSeason(Season season) {
        return season.ordinal() < Season.values().length - 1 ? Season.values()[season.ordinal() + 1] : Season.values()[0];
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        for (int i = 0; i < 7; i++) {
            forecast[i] = Weather.values()[nbt.getCompoundTag("Forecast").getByte("Day" + i)];
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound tag = new NBTTagCompound();
        for (int i = 0; i < 7; i++) {
            Weather weather = forecast[i];
            if (weather == null) weather = Weather.SUNNY;
            tag.setByte("Day" + i, (byte) weather.ordinal());
        }

        nbt.setTag("Forecast", tag);

        return nbt;
    }
}