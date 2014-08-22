package harvestmoon.commands;

import static harvestmoon.HarvestMoon.handler;
import harvestmoon.network.PacketHandler;
import harvestmoon.network.PacketSetCalendar;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class CommandDay implements ICommand {
    @Override
    public int compareTo(Object o) {
        return 0;
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
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if(parameters == null || parameters.length != 1) return;
        try{
            PacketHandler.sendToServer(new PacketSetCalendar(handler.getClient().getCalendar().getDate().setDay(Integer.parseInt(parameters[0]))));
        } catch (NumberFormatException e) {}
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        ArrayList<String> string = new ArrayList();
        for(int i = 0; i < 30; i++) {
            string.add("" + i);
        }

        return string;
    }

    @Override
    public boolean isUsernameIndex(String[] parameter, int p_82358_2_) {
        return false;
    }
}
