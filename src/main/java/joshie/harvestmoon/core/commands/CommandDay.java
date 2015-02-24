package joshie.harvestmoon.core.commands;

import java.util.ArrayList;
import java.util.List;

import joshie.harvestmoon.core.config.Calendar;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

public class CommandDay extends CommandBase {
    private static List days;

    static {
        days = new ArrayList(Calendar.DAYS_PER_SEASON);
        for (int i = 1; i <= Calendar.DAYS_PER_SEASON; i++) {
            days.add("" + i);
        }
    }

    @Override
    public String getCommandName() {
        return "day";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/day <day>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if (parameters == null || parameters.length != 1) return;
        try {
            PacketHandler.sendToServer(new PacketSetCalendar(CalendarHelper.getClientDate().setDay(Integer.parseInt(parameters[0]))));
        } catch (NumberFormatException e) {}
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        return days;
    }
}
