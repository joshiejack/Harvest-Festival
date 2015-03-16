package joshie.harvestmoon.core.commands;

import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

public class HMCommandYear extends HMCommandBase {
    @Override
    public String getCommandName() {
        return "year";
    }

    @Override
    public String getUsage() {
        return "<year>";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                PacketHandler.sendToServer(new PacketSetCalendar(CalendarHelper.getClientDate().setYear(Integer.parseInt(parameters[0]))));
                return true;
            } catch (NumberFormatException e) {}
        }

        return false;
    }
}
