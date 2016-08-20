package joshie.harvest.core.commands;

import joshie.harvest.api.HFCommand;
import joshie.harvest.api.HFRegister;
import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.stats.StatDataServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@HFRegister
public class HFCommandGold extends HFCommand {
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
                StatDataServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTracker(player).getStats();
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
