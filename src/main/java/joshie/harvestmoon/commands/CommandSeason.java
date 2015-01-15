package joshie.harvestmoon.commands;

import static joshie.harvestmoon.HarvestMoon.handler;

import java.util.ArrayList;
import java.util.List;

import joshie.harvestmoon.calendar.Season;
import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSetCalendar;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

import org.apache.commons.lang3.StringUtils;

public class CommandSeason implements ICommand {
    @Override
    public int compareTo(Object o) {
        return 0;
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
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if(parameters == null || parameters.length != 1) return;
        for(Season s: Season.values()) {
            if(StringUtils.equalsIgnoreCase(s.name(), parameters[0])) {                
                PacketHandler.sendToServer(new PacketSetCalendar(handler.getClient().getCalendar().getDate().setSeason(s)));
                break;
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        ArrayList<String> string = new ArrayList();
        string.add("spring");
        string.add("summer");
        string.add("autumn");
        string.add("winter");
        return string;
    }

    @Override
    public boolean isUsernameIndex(String[] parameter, int p_82358_2_) {
        return false;
    }
}
