package joshie.harvestmoon.commands;

import java.util.ArrayList;
import java.util.List;

import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.helpers.CalendarHelper;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSetCalendar;
import net.minecraft.command.ICommandSender;

import org.apache.commons.lang3.StringUtils;

public class CommandSeason extends CommandBase {
    private static final List seasons;

    static {
        seasons = new ArrayList(4);
        seasons.add("spring");
        seasons.add("summer");
        seasons.add("autumn");
        seasons.add("winter");
    }

    @Override
    public String getCommandName() {
        return "season";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/season <season>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if (parameters == null || parameters.length != 1) return;
        for (Season s : Season.values()) {
            if (StringUtils.equalsIgnoreCase(s.name(), parameters[0])) {
                PacketHandler.sendToServer(new PacketSetCalendar(CalendarHelper.getClientDate().setSeason(s)));
                break;
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        return seasons;
    }
}
