package joshie.harvest.core.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import static joshie.harvest.core.config.Calendar.TICKS_PER_DAY;

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
        long i = sender.getEntityWorld().getWorldTime() + TICKS_PER_DAY;
        sender.getEntityWorld().setWorldTime((i - i % TICKS_PER_DAY) - 1);
        return true;
    }
}