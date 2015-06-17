package joshie.harvest.core.commands;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

import org.apache.commons.lang3.StringUtils;

public class HFCommandSeason extends HFCommandBase  {
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
                    PacketHandler.sendToServer(new PacketSetCalendar(HFTrackers.getCalendar().getDate().setSeason(s)));
                    return true;
                }
            }
        }

        return false;
    }
}
