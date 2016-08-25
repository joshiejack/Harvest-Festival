package joshie.harvest.calendar.command;

import joshie.harvest.api.HFCommand;
import joshie.harvest.api.HFQuest;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

@HFQuest
public class HFCommandNewDay extends HFCommand {
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