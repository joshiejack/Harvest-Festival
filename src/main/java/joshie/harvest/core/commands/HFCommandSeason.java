package joshie.harvest.core.commands;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.Calendar;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

public class HFCommandSeason extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "season";
    }

    @Override
    public String getUsage() {
        return "<season>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            for (Season season : Season.values()) {
                if (StringUtils.equalsIgnoreCase(season.name(), parameters[0])) {
                    Calendar calendar = HFTrackers.getCalendar(sender.getEntityWorld());
                    calendar.getDate().setSeason(season);
                    PacketHandler.sendToEveryone(new PacketSetCalendar(sender.getEntityWorld().provider.getDimension(), calendar.getDate()));
                    return true;
                }
            }
        }
        return false;
    }
}