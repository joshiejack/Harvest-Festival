package joshie.harvest.player.command;

import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.AbstractHFCommand.HFCommand;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.stats.StatsServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@HFCommand
public class HFCommandGold extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "gold";
    }

    @Override
    public String getUsage() {
        return "<add|set> amount";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1 || parameters.length == 2) {
            try {
                long amount = parameters.length == 1 ? Long.parseLong(parameters[0]) : Long.parseLong(parameters[1]);
                boolean set = !(parameters.length == 1 || parameters[0].equals("add"));

                EntityPlayerMP player = ((EntityPlayerMP) sender);
                StatsServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTracker(player).getStats();
                if (set) {
                    stats.setGold(player, amount);
                } else stats.addGold(player, amount);

                return true; //After succesfully completing the command, return to avoid throwing an error
            } catch (NumberFormatException ignored) {
            }
        }
        return false;
    }
}
