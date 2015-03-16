package joshie.harvestmoon.core.commands;

import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

public class HMCommandDay extends HMCommandBase  {
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
