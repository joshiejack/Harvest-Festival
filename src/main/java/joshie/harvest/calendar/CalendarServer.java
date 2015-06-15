package joshie.harvest.calendar;

import java.util.List;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.core.helpers.CropHelper;
import joshie.harvest.core.helpers.MineHelper;
import joshie.harvest.core.helpers.PlayerHelper;
import joshie.harvest.core.helpers.ServerHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CalendarServer extends Calendar {
    @Override
    public void newDay() {
        int day = date.getDay();
        Season season = date.getSeason();
        int year = date.getYear();

        if (day < joshie.harvest.core.config.Calendar.DAYS_PER_SEASON) {
            day++;
        } else {
            season = getNextSeason();
            day = 1;
            if (season == Season.SPRING) {
                year++;
            }
        }

        date.setDay(day).setSeason(season).setYear(year);
        PacketHandler.sendToEveryone(new PacketSetCalendar(date));

        CropHelper.newDay();
        DataHelper.getAnimalTracker().newDay();
        MineHelper.newDay();

        //Loop through all the players and do stuff related to them, Pass the world that the player is in
        for (EntityPlayer player : (List<EntityPlayer>) MinecraftServer.getServer().getConfigurationManager().playerEntityList) {
            PlayerHelper.getData(player).newDay();
        }

        ServerHelper.markDirty();
    }

    private Season getNextSeason() {
        return date.getSeason().ordinal() < Season.values().length - 1 ? Season.values()[date.getSeason().ordinal() + 1] : Season.values()[0];
    }

    public void readFromNBT(NBTTagCompound nbt) {
        date.readFromNBT(nbt);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        date.writeToNBT(nbt);
    }
}
