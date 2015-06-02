package joshie.harvest.core.commands;

import joshie.harvest.core.helpers.CalendarHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

public class HFCommandDay extends HFCommandBase  {  
    @Override
    public String getCommandName() {
        return "day";
    }

    @Override
    public String getUsage() {
        return "<day>";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                PacketHandler.sendToServer(new PacketSetCalendar(CalendarHelper.getClientDate().setDay(Integer.parseInt(parameters[0]))));
                return true;
            } catch (NumberFormatException e) {}
        }

        return false;
    }
}
