package joshie.harvest.core.commands;

import joshie.harvest.calendar.Calendar;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class HFCommandDay extends HFCommandBase {
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
                Calendar calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                int day = Math.min(joshie.harvest.core.config.Calendar.DAYS_PER_SEASON, Math.max(1, Integer.parseInt(parameters[0])));


                calendar.getDate().setDay(day);
                PacketHandler.sendToEveryone(new PacketSetCalendar(sender.getEntityWorld().provider.getDimension(), calendar.getDate()));
                return true;
            } catch (NumberFormatException ignored) {
            }
        }
        return false;
    }
}