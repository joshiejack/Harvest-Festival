package joshie.harvest.calendar.command;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.data.CalendarServer;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.core.HFTrackers;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import static joshie.harvest.api.calendar.CalendarDate.DAYS_PER_SEASON;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandDay extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "day";
    }

    @Override
    public String getUsage() {
        return "/hf day <value>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                CalendarServer calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                int day = Math.min(DAYS_PER_SEASON, Math.max(1, parseInt(parameters[0]))) - 1;
                Season season = calendar.getDate().getSeason();
                int year = calendar.getDate().getYear() + 1;
                long leftover = server.worldServers[0].getWorldTime() % HFCalendar.TICKS_PER_DAY;
                CalendarHelper.setWorldTime(server, CalendarHelper.getTime(day, season, year) + leftover);
                return true;
            } catch (NumberFormatException ignored) {}
        }
        return false;
    }
}