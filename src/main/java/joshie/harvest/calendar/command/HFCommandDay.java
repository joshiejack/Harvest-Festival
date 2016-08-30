package joshie.harvest.calendar.command;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import static joshie.harvest.calendar.HFCalendar.DAYS_PER_SEASON;

@HFCommand
public class HFCommandDay extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "day";
    }

    @Override
    public String getUsage() {
        return "<day>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                CalendarServer calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                int day = Math.min(DAYS_PER_SEASON, Math.max(1, parseInt(parameters[0]))) - 1;
                Season season = calendar.getDate().getSeason();
                int year = Math.max(1, calendar.getDate().getYear());
                long time = CalendarHelper.getTime(day, season, year);
                sender.getEntityWorld().setWorldTime(time);
                calendar.recalculateAndUpdate(sender.getEntityWorld());
                return true;
            } catch (NumberFormatException ignored) {}
        }
        return false;
    }
}