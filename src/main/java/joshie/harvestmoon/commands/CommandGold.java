package joshie.harvestmoon.commands;

import joshie.harvestmoon.network.PacketGoldCommand;
import joshie.harvestmoon.network.PacketHandler;
import net.minecraft.command.ICommandSender;

public class CommandGold extends CommandBase {
    @Override
    public String getCommandName() {
        return "gold";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/gold <gold>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if (parameters == null || parameters.length < 1 || parameters.length > 2) return;
        try {
            int amount = parameters.length == 1 ? Integer.parseInt(parameters[0]) : Integer.parseInt(parameters[1]);
            boolean set = parameters.length == 1 || parameters[0].equals("add") ? false : true;
            PacketHandler.sendToServer(new PacketGoldCommand(amount, set));
        } catch (NumberFormatException e) {}
    }
}
