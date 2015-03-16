package joshie.harvestmoon.core.commands;

import joshie.harvestmoon.core.network.PacketHandler;
import joshie.harvestmoon.core.network.PacketNewDay;
import net.minecraft.command.ICommandSender;

public class HMCommandNewDay extends HMCommandBase {
    @Override
    public String getCommandName() {
        return "newDay";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        PacketHandler.sendToServer(new PacketNewDay());
        return true;
    }
}
