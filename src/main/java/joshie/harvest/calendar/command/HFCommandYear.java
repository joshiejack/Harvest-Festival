package joshie.harvest.calendar.command;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

@HFCommand
public class HFCommandYear extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "year";
    }

    @Override
    public String getUsage() {
        return "Usage: /hf year <value>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                CalendarServer calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                int day = calendar.getDate().getDay();
                Season season = calendar.getDate().getSeason();
                int year = Math.min(Integer.MAX_VALUE, Math.max(1, Integer.parseInt(parameters[0])));
                long leftover = server.worldServers[0].getWorldTime() % HFCalendar.TICKS_PER_DAY;
                sender.getEntityWorld().setWorldTime(CalendarHelper.getTime(day, season, year) + leftover);
                calendar.recalculateAndUpdate(sender.getEntityWorld());
                return true;
            } catch (NumberFormatException ignored) {
            }
        }

        return false;
    }
}