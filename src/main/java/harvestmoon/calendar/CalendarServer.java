package harvestmoon.calendar;

import static harvestmoon.HarvestMoon.handler;
import static harvestmoon.network.PacketHandler.sendToEveryone;
import harvestmoon.network.PacketSetCalendar;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldSavedData;

public class CalendarServer extends WorldSavedData {
    public static final String DATA_NAME = "HM-Calendar";
    public static final int DAYS_PER_SEASON = 30;
    private static boolean loaded = false;

    private CalendarDate date = new CalendarDate(1, Season.SPRING, 1);

    public CalendarServer(String string) {
        super(string);
    }

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

        if (day < DAYS_PER_SEASON) {
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
        
        handler.getServer().getAnimalTracker().newDay();
        handler.getServer().getCropTracker().newDay();
        
        for(EntityPlayer player: (List<EntityPlayer>)MinecraftServer.getServer().getEntityWorld().playerEntities) {
            handler.getServer().getPlayerData(player).newDay((EntityPlayerMP)player);
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
