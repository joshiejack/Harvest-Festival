package joshie.harvest.core.commands;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.CalendarHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

public class HFCommandSeason extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "season";
    }

    @Override
    public String getUsage() {
        return "<season>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            for (Season season : Season.values()) {
                if (StringUtils.equalsIgnoreCase(season.name(), parameters[0])) {
                    Calendar calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                    int day = calendar.getDate().getDay();
                    int year = Math.max(1, calendar.getDate().getYear());
                    sender.getEntityWorld().setWorldTime(CalendarHelper.getTime(day, season, year));
                    calendar.recalculateAndUpdate();
                    return true;
                }
            }
        }
        return false;
    }
}