package joshie.harvest.calendar.command;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.calendar.data.CalendarServer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandSeason extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "season";
    }

    @Override
    public String getUsage() {
        return "/hf season <spring|summer|autumn|winter>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            for (Season season : Season.values()) {
                if (StringUtils.equalsIgnoreCase(season.name(), parameters[0])) {
                    CalendarServer calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                    int day = calendar.getDate().getDay();
                    int year = calendar.getDate().getYear() + 1;
                    long leftover = server.worlds[0].getWorldTime() % HFCalendar.TICKS_PER_DAY;
                    CalendarHelper.setWorldTime(server, CalendarHelper.getTime(day, season, year) + leftover);
                    return true;
                }
            }
        }
        return false;
    }
}