package joshie.harvest.calendar;

import static joshie.harvest.core.helpers.ServerHelper.markDirty;
import static joshie.harvest.core.network.PacketHandler.sendToEveryone;

import java.util.List;

import joshie.harvest.core.config.Calendar;
import joshie.harvest.core.helpers.AnimalHelper;
import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.core.helpers.MineHelper;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.network.PacketSetCalendar;
import joshie.harvest.core.util.IData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CalendarServer implements IData {
    private boolean loaded = false;

    private CalendarDate date = new CalendarDate(0, Season.SPRING, 1);

    public CalendarDate getDate() {
        return date;
    }

    public void setDate(int day, Season season, int year) {
        date.setDay(day).setSeason(season).setYear(year);
        markDirty();
    }

    //Increases the day
    public boolean newDay() {
        int day = date.getDay();
        Season season = date.getSeason();
        int year = date.getYear();

        if (day < Calendar.DAYS_PER_SEASON) {
            day++;
        } else {
            season = getNextSeason();
            day = 1;
            if (season == Season.SPRING) {
                year++;
            }
        }

        date.setDay(day).setSeason(season).setYear(year);
        sendToEveryone(new PacketSetCalendar(date));

        CropHelper.newDay();
        AnimalHelper.newDay();
        MineHelper.newDay();

        //Loop through all the players and do stuff related to them, Pass the world that the player is in
        for (EntityPlayer player : (List<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            PlayerHelper.getData(player).newDay();
        }

        loaded = true;
        markDirty();
        return true;
    }

    //Returns the season after the present one
    public Season getNextSeason() {
        return date.getSeason().ordinal() < Season.values().length - 1 ? Season.values()[date.getSeason().ordinal() + 1] : Season.values()[0];
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        date.readFromNBT(nbt);
        loaded = nbt.getBoolean("Loaded");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        date.writeToNBT(nbt);
        nbt.setBoolean("Loaded", true);
    }
}
