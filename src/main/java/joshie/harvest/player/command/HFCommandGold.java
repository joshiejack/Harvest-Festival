package joshie.harvest.player.command;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.stats.StatsServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandGold extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "gold";
    }

    @Override
    public String getUsage() {
        return "/hf gold [player] <add|set> <value>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && (parameters.length == 2 || parameters.length == 3)) {
            try {
                long amount = Long.parseLong(parameters[parameters.length - 1]);
                boolean set = ((parameters.length == 2 && parameters[0].equals("set")) || (parameters.length == 3 && parameters[1].equals("set")));
                EntityPlayerMP player = parameters.length == 2? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
                StatsServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats();
                if (set) {
                    stats.setGold(player, amount);
                } else stats.addGold(player, amount);

                return true; //After succesfully completing the command, return to avoid throwing an error
            } catch (NumberFormatException | CommandException ignored) {}
        }
        return false;
    }
}
