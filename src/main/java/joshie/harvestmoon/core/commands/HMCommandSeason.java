package joshie.harvestmoon.core.commands;

import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.core.helpers.CalendarHelper;
import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

import org.apache.commons.lang3.StringUtils;

public class HMCommandSeason extends HMCommandBase  {
    @Override
    public String getCommandName() {
        return "season";
    }

    @Override
    public String getUsage() {
        return "<season>";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            for (Season s : Season.values()) {
                if (StringUtils.equalsIgnoreCase(s.name(), parameters[0])) {
                    PacketHandler.sendToServer(new PacketSetCalendar(CalendarHelper.getClientDate().setSeason(s)));
                    return true;
                }
            }
        }

        return false;
    }
}
