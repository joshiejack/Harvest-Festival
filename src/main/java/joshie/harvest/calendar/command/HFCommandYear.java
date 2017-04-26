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

@HFCommand
@SuppressWarnings("unused")
public class HFCommandYear extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "year";
    }

    @Override
    public String getUsage() {
        return "/hf year <value>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                CalendarServer calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                int day = calendar.getDate().getDay();
                Season season = calendar.getDate().getSeason();
                int year = Math.min(Integer.MAX_VALUE, Math.max(1, Integer.parseInt(parameters[0])));
                long leftover = server.worlds[0].getWorldTime() % HFCalendar.TICKS_PER_DAY;
                CalendarHelper.setWorldTime(server, CalendarHelper.getTime(day, season, year) + leftover);
                return true;
            } catch (NumberFormatException ignored) {
            }
        }

        return false;
    }
}