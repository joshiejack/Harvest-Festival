package joshie.harvest.core.commands;

import joshie.harvest.core.handlers.events.FMLEvents;
import net.minecraft.command.ICommandSender;

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
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        FMLEvents.newDay(true);
        return true;
    }
}
