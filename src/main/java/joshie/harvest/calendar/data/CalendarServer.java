package joshie.harvest.calendar.data;

import joshie.harvest.api.calendar.CalendarDate;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.CalendarAPI;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.calendar.packet.PacketSyncCalendar;
import joshie.harvest.calendar.packet.PacketSyncForecast;
import joshie.harvest.calendar.packet.PacketSyncStrength;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

import static joshie.harvest.api.calendar.CalendarDate.DAYS_PER_SEASON;
import static joshie.harvest.api.calendar.Season.*;

public class CalendarServer extends Calendar {
    private final CalendarDate DATE = new CalendarDate(0, SPRING, 1);
    private static final Random rand = new Random();
    private CalendarSavedData data;

    public CalendarServer() {}

    public void setWorld(CalendarSavedData data, World world) {
        this.data = data;
        super.setWorld(world);
        recalculateAndUpdate(world);
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

    public void syncToPlayer(EntityPlayer player) {
        PacketHandler.sendToClient(new PacketSyncCalendar(DATE), player);
        PacketHandler.sendToClient(new PacketSyncForecast(forecast), player);
        PacketHandler.sendToClient(new PacketSyncStrength(rainStrength, stormStrength), player);
    }

    public void recalculate(World world) {
        CalendarHelper.setDate(world, DATE);
        HFTrackers.markCalendarDirty();
    }

    public void recalculateAndUpdate(World world) {
        recalculate(world); //Recalc first
        PacketHandler.sendToEveryone(new PacketSyncCalendar(DATE)); //Sync the new date
    }

    /* ############# Weather ################*/
    public void setTodaysWeather(Weather weather) {
        forecast[0] = weather;
        updateWeatherStrength();
        HFTrackers.markCalendarDirty();
        PacketHandler.sendToEveryone(new PacketSyncForecast(forecast));
    }

    private Weather getRandomWeather(int day, Season season) {
        if (day > DAYS_PER_SEASON) {
            season = getNextSeason(season);
        }

        Weather weather = CalendarAPI.INSTANCE.getDataForSeason(season).getWeather(rand);
        return isWeatherEnabled(weather) ? weather : Weather.SUNNY;
    }

    private boolean isWeatherEnabled(Weather weather) {
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
        PacketHandler.sendToEveryone(new PacketSyncForecast(forecast));
    }

    /* ############# Saving ################*/
    public void markDirty() {
        data.markDirty();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("Date")) {
            CalendarDate date = CalendarDate.fromNBT(nbt.getCompoundTag("Date"));
            DATE.setDate(date.getDay(), date.getSeason(), date.getYear());
        }

        rainStrength = nbt.getFloat("Rain");
        stormStrength = nbt.getFloat("Storm");
        for (int i = 0; i < 7; i++) {
            forecast[i] = Weather.values()[nbt.getByte("Day" + i)];
            if (forecast[i] == null) {
                forecast[i] = Weather.SUNNY;
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("Date", DATE.toNBT());
        nbt.setFloat("Rain", rainStrength);
        nbt.setFloat("Storm", stormStrength);
        for (int i = 0; i < 7; i++) {
            Weather weather = forecast[i];
            if (weather == null) weather = Weather.SUNNY;
            nbt.setByte("Day" + i, (byte) weather.ordinal());
        }

        return nbt;
    }
}