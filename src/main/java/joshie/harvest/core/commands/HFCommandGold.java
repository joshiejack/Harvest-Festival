package joshie.harvest.core.commands;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.player.stats.StatDataServer;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class HFCommandGold extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "gold";
    }

    @Override
    public String getUsage() {
        return "<add|set> amount";
    }

    @Override
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1 || parameters.length == 2) {
            try {
                long amount = parameters.length == 1 ? Long.parseLong(parameters[0]) : Long.parseLong(parameters[1]);
                boolean set = parameters.length == 1 || parameters[0].equals("add") ? false : true;

                EntityPlayerMP player = getPlayer(sender);
                StatDataServer stats = HFTrackers.getServerPlayerTracker(player).getStats();
                if (set) {
                    stats.setGold(player, amount);
                } else stats.addGold(player, amount);
                
                return true; //After succesfully completing the command, return to avoid throwing an error
            } catch (NumberFormatException e) {}
        }

        return false;
    }
}
