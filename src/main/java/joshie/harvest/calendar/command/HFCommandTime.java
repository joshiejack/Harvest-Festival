package joshie.harvest.calendar.command;

import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

@HFCommand
public class HFCommandTime extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "time";
    }

    @Override
    public String getUsage() {
        return "Usage: /hf time <set|add> <value>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters.length > 1) {
            if (parameters[0].equals("set")) {
                long time = CalendarHelper.getElapsedDays(server.worldServers[0].getWorldTime()) * HFCalendar.TICKS_PER_DAY;
                if (parameters[1].equals("day"))  {
                    time += 3000;
                } else if (parameters[1].equals("night")) {
                    time += 18000;
                } else {
                    time += (parseInt(parameters[1]) - 6000L);
                }

                for (int i = 0; i < server.worldServers.length; ++i) {
                    WorldServer worldserver = server.worldServers[i];
                    worldserver.setWorldTime(time);
                }

                //TODO: Make set time also add time
                HFTrackers.<CalendarServer>getCalendar(server.worldServers[0]).recalculateAndUpdate(server.worldServers[0]);
                return true;
            }

            if (parameters[0].equals("add")) {
                int l = parseInt(parameters[1]);
                for (int i = 0; i < server.worldServers.length; ++i) {
                    WorldServer worldserver = server.worldServers[i];
                    worldserver.setWorldTime(worldserver.getWorldTime() + l);
                }

                HFTrackers.<CalendarServer>getCalendar(server.worldServers[0]).recalculateAndUpdate(server.worldServers[0]);
                return true;
            }
        }

        return false;
    }
}