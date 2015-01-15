package joshie.harvestmoon.commands;

import static joshie.harvestmoon.HarvestMoon.handler;

import java.util.ArrayList;
import java.util.List;

import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSetCalendar;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public class CommandYear implements ICommand {
    @Override
    public int compareTo(Object o) {
        return 0;
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
    public List getCommandAliases() {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if(parameters == null || parameters.length != 1) return;
        try{
            PacketHandler.sendToServer(new PacketSetCalendar(handler.getClient().getCalendar().getDate().setYear(Integer.parseInt(parameters[0]))));
        } catch (NumberFormatException e) {}
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        ArrayList<String> string = new ArrayList();
        for(int i = 0; i < Byte.MAX_VALUE; i++) {
            string.add("" + i);
        }

        return string;
    }

    @Override
    public boolean isUsernameIndex(String[] parameter, int p_82358_2_) {
        return false;
    }
}
