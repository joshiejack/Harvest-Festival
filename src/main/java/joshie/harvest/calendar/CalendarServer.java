package joshie.harvest.calendar;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.packets.PacketSetCalendar;
import joshie.harvest.calendar.packets.PacketSyncForecast;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

import static joshie.harvest.api.calendar.Season.*;
import static joshie.harvest.calendar.HFCalendar.DAYS_PER_SEASON;

public class CalendarServer extends Calendar {
    private CalendarDate DATE = new CalendarDate(0, SPRING, 1);
    private static final Random rand = new Random();

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
    }

    @Override
    public CalendarDate getDate() {
        return DATE;
    }

    public void newDay(World world) {
        recalculateAndUpdate(world); //Update the date
        Weather[] newForecast = new Weather[7];
        System.arraycopy(forecast, 1, newForecast, 0, 6);
        forecast = newForecast;
        updateForecast();
    }

    private Season getNextSeason(Season season) {
        if (season == SPRING) return SUMMER;
        else if (season == SUMMER) return AUTUMN;
        else if (season == AUTUMN) return WINTER;
        else return SPRING;
    }

    private void recalculate(World world) {
        CalendarHelper.setDate(world, DATE);
        HFTrackers.markCalendarDirty();
    }

    public void recalculateAndUpdate(World world) {
        recalculate(world); //Recalc first
        PacketHandler.sendToDimension(getDimension(), new PacketSetCalendar(DATE)); //Sync the new date
    }

    /* ############# Weather ################*/
    public void setTodaysWeather(Weather weather) {
        forecast[0] = weather;
        updateForecast();
    }

    private Weather getRandomWeather(int day, Season season) {
        if (day > DAYS_PER_SEASON) {
            season = getNextSeason(season);
        }

        SeasonData data = CalendarAPI.INSTANCE.getDataForSeason(season);
        for (Weather weather : Weather.values()) {
            if (isWeatherDisabled(weather)) continue;
            double chance = data.getWeatherChance(weather);
            if (rand.nextDouble() * 100D < chance) {
                return weather;
            }
        }

        return Weather.SUNNY;
    }

    private boolean isWeatherDisabled(Weather weather) {
        switch (weather) {
            case SUNNY:
                return HFCalendar.ENABLE_SUNNY;
            case RAIN:
                return HFCalendar.ENABLE_RAIN;
            case TYPHOON:
                return HFCalendar.ENABLE_TYPHOON;
            case SNOW:
                return HFCalendar.ENABLE_SNOW;
            case BLIZZARD:
                return HFCalendar.ENABLE_BLIZZARD;
            default:
                return false;
        }
    }

    private void updateForecast() {
        //If they're null set them
        for (int i = 0; i < 7; i++) {
            if (forecast[i] == null) {
                forecast[i] = getRandomWeather(getDate().getDay() + i, getDate().getSeason());
            }
        }

        updateWeatherStrength();
        HFTrackers.markCalendarDirty();
        PacketHandler.sendToDimension(getDimension(), new PacketSyncForecast(forecast));
    }

    /* ############# Saving ################*/
    public void readFromNBT(NBTTagCompound nbt) {
        for (int i = 0; i < 7; i++) {
            forecast[i] = Weather.values()[nbt.getCompoundTag("Forecast").getByte("Day" + i)];
        }
    }

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