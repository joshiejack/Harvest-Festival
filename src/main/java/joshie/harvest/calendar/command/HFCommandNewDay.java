package joshie.harvest.calendar.command;

import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

@HFCommand
public class HFCommandNewDay extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "newDay";
    }

    @Override
    public String getUsage() {
        return "Usage: /hf newDay";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        long i = sender.getEntityWorld().getWorldTime() + TICKS_PER_DAY;
        for (int j = 0; j < server.worldServers.length; ++j) {
            WorldServer worldserver = server.worldServers[j];
            worldserver.setWorldTime((i - i % TICKS_PER_DAY) - 1);
        }

        return true;
    }
}