package joshie.harvestmoon.core.commands;

import joshie.harvestmoon.core.network.PacketGoldCommand;
import joshie.harvestmoon.core.network.PacketHandler;
import net.minecraft.command.ICommandSender;

public class HMCommandGold extends HMCommandBase {
    @Override
    public String getCommandName() {
        return "gold";
    }

    @Override
    public String getUsage() {
        return "<add|set> amount";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1 || parameters.length == 2) {
            try {
                long amount = parameters.length == 1 ? Long.parseLong(parameters[0]) : Long.parseLong(parameters[1]);
                boolean set = parameters.length == 1 || parameters[0].equals("add") ? false : true;
                PacketHandler.sendToServer(new PacketGoldCommand(amount, set));
                return true; //After succesfully completing the command, return to avoid throwing an error
            } catch (NumberFormatException e) {}
        }

        return false;
    }
}
