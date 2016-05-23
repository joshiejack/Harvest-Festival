package joshie.harvest.core.commands;

import joshie.harvest.calendar.Calendar;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class HFCommandYear extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "year";
    }

    @Override
    public String getUsage() {
        return "<year>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                Calendar calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                int year = Math.min(Integer.MAX_VALUE, Math.max(1, Integer.parseInt(parameters[0])));
                calendar.getDate().setYear(year);
                PacketHandler.sendToEveryone(new PacketSetCalendar(sender.getEntityWorld().provider.getDimension(), calendar.getDate()));
                return true;
            } catch (NumberFormatException ignored) {
            }
        }

        return false;
    }
}