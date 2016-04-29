package joshie.harvest.core.commands;

import joshie.harvest.core.handlers.EventsHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class HFCommandNewDay extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "newDay";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        EventsHandler.newDay(sender.getEntityWorld(), true);
        return true;
    }
}