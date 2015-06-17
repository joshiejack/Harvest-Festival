package joshie.harvest.core.commands;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSyncGold;
import joshie.harvest.player.PlayerStats;
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
                PlayerStats stats = HFTrackers.getPlayerTracker(player).getStats();
                if (set) {
                    stats.setGold(amount);
                } else stats.addGold(amount);

                PacketHandler.sendToClient(new PacketSyncGold(stats.getGold()), (EntityPlayerMP) player);
                return true; //After succesfully completing the command, return to avoid throwing an error
            } catch (NumberFormatException e) {}
        }

        return false;
    }
}
