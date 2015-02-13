package joshie.harvestmoon.commands;

import static joshie.harvestmoon.HarvestMoon.handler;

import java.util.ArrayList;
import java.util.List;

import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

public class CommandYear extends CommandBase {
    private static List years;

    static {
        years = new ArrayList(Byte.MAX_VALUE);
        for (int i = 1; i < Byte.MAX_VALUE; i++) {
            years.add("" + i);
        }
    }

    @Override
    public String getCommandName() {
        return "year";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/year <year>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if (parameters == null || parameters.length != 1) return;
        try {
            PacketHandler.sendToServer(new PacketSetCalendar(handler.getClient().getCalendar().getDate().setYear(Integer.parseInt(parameters[0]))));
        } catch (NumberFormatException e) {}
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        return years;
    }
}
