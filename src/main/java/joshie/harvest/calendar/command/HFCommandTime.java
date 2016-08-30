package joshie.harvest.calendar.command;

import joshie.harvest.calendar.CalendarServer;
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
        return "set|add time";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters.length > 1) {
            if (parameters[0].equals("set")) {
                int i1;

                if (parameters[1].equals("day"))  {
                    i1 = 8000;
                } else if (parameters[1].equals("night")) {
                    i1 = 18000;
                } else {
                    i1 = parseInt(parameters[1]);
                }

                for (int i = 0; i < server.worldServers.length; ++i) {
                    WorldServer worldserver = server.worldServers[i];
                    worldserver.setWorldTime(i1);
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