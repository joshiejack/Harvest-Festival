package joshie.harvestmoon.core.commands;

import java.util.List;

import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketNewDay;
import net.minecraft.command.ICommandSender;

public class CommandNewDay extends CommandBase {
    @Override
    public String getCommandName() {
        return "newDay";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/newDay";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        PacketHandler.sendToServer(new PacketNewDay());
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        return null;
    }
}
