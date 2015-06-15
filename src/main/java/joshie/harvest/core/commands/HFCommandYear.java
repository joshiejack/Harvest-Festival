package joshie.harvest.core.commands;

import joshie.harvest.core.handlers.DataHelper;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

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
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            try {
                PacketHandler.sendToServer(new PacketSetCalendar(DataHelper.getCalendar().getDate().setYear(Integer.parseInt(parameters[0]))));
                return true;
            } catch (NumberFormatException e) {}
        }

        return false;
    }
}
