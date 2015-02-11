package joshie.harvestmoon.commands;

import joshie.harvestmoon.network.PacketHandler;
import joshie.harvestmoon.network.PacketSetDifficulty;
import net.minecraft.command.ICommandSender;

public class CommandMode extends CommandBase {
    @Override
    public String getCommandName() {
        return "easy";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/easy [true|false]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if (parameters == null || parameters.length != 1) return;
        try {
            boolean easy = Boolean.parseBoolean(parameters[0]);
            PacketHandler.sendToServer(new PacketSetDifficulty(easy, true));
        } catch (NumberFormatException e) {}
    }
}
