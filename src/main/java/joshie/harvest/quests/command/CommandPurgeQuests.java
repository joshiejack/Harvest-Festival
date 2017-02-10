package joshie.harvest.quests.command;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

@HFCommand
public class CommandPurgeQuests extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "purge";
    }

    @Override
    public String getUsage() {
        return "/hf purge [player]";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && (parameters.length == 0 || parameters.length == 1)) {
            try {
                EntityPlayerMP player = parameters.length == 0? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
                HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getQuests().purge(player);
                TownHelper.<TownDataServer>getClosestTownToEntity(player).getQuests().purge(player);
                return true; //After succesfully completing the command, return to avoid throwing an error
            } catch (NumberFormatException | PlayerNotFoundException ignored) {}
        }
        return false;
    }
}
