package joshie.harvest.quests.command;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.town.TownHelper;
import joshie.harvest.town.data.TownDataServer;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

@HFCommand
public class CommandPurgeQuests extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "purge";
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "/hf purge [player]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 0 || parameters.length == 1) {
            EntityPlayerMP player = parameters.length == 0? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
            HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getQuests().purge(player);
            TownHelper.<TownDataServer>getClosestTownToEntity(player, false).getQuests().purge(player);
        } else throw new WrongUsageException(getCommandUsage(sender));
    }
}
