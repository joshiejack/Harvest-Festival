package joshie.harvest.calendar;

import java.util.List;
import java.util.Random;

import joshie.harvest.api.HFApi;
import joshie.harvest.api.calendar.Season;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.api.core.ISeasonData;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.core.network.PacketSyncForecast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CalendarServer extends Calendar {
    private static final Random rand = new Random();

    @Override
    public void setTodaysWeather(Weather weather) {
        forecast[0] = weather;
        updateForecast();
    }

    public Weather getRandomWeather(int day, Season season) {
        if (day > joshie.harvest.core.config.Calendar.DAYS_PER_SEASON) {
            season = getNextSeason(season);
        }

        ISeasonData data = HFApi.CALENDAR.getDataForSeason(season);
        for (Weather weather : Weather.values()) {
            double chance = data.getWeatherChance(weather);
            if (rand.nextDouble() * 100D < chance) {
                return weather;
            }
        }

        return Weather.SUNNY;
    }
    
    @Override
    public void updateForecast() {
      //If they're null set them
        for (int i = 0; i < 7; i++) {
            if (forecast[i] == null) {
                forecast[i] = getRandomWeather(date.getDay() + i, date.getSeason());
            }
        }
        
        PacketHandler.sendToEveryone(new PacketSyncForecast(forecast));
    }

    @Override
    public void newDay() {
        int day = date.getDay();
        Season season = date.getSeason();
        int year = date.getYear();

        if (day < joshie.harvest.core.config.Calendar.DAYS_PER_SEASON) {
            day++;
        } else {
            season = getNextSeason(season);
            day = 1;
            if (season == Season.SPRING) {
                year++;
            }
        }

        date.setDay(day).setSeason(season).setYear(year);
        PacketHandler.sendToEveryone(new PacketSetCalendar(date));

        HFTrackers.getCropTracker().newDay();
        HFTrackers.getAnimalTracker().newDay();
        HFTrackers.getMineTracker().newDay();

        /** Setup the forecast for the next 7 days **/
        ISeasonData data = date.getSeasonData();
        Weather[] newForecast = new Weather[7];

        //Copy over the old forecast
        for (int i = 1; i <= 6; i++) {
            newForecast[i - 1] = forecast[i];
        }

        forecast = newForecast;
        updateForecast();

        //Loop through all the players and do stuff related to them, Pass the world that the player is in
        for (EntityPlayer player : (List<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            HFTrackers.getPlayerTracker(player).newDay();
        }

        HFTrackers.markDirty();
    }

    private Season getNextSeason(Season season) {
        return season.ordinal() < Season.values().length - 1 ? Season.values()[season.ordinal() + 1] : Season.values()[0];
    }

    public void readFromNBT(NBTTagCompound nbt) {
        date.readFromNBT(nbt);
        for (int i = 0; i < 7; i++) {
            forecast[i] = Weather.values()[nbt.getByte("ForecastDay" + i)];
        }
    }

    public void writeToNBT(NBTTagCompound nbt) {
        date.writeToNBT(nbt);
        for (int i = 0; i < 7; i++) {
            Weather weather = forecast[i];
            if (weather == null) weather = Weather.SUNNY;
            nbt.setByte("ForecastDay" + i, (byte) weather.ordinal());
        }
    }
}
