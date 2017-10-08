package joshie.harvest.player.command;

import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.stats.StatsServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandGold extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "gold";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf gold [player] <add|set> <value>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 2 || parameters.length == 3) {
            long amount = Long.parseLong(parameters[parameters.length - 1]);
            boolean set = ((parameters.length == 2 && parameters[0].equals("set")) || (parameters.length == 3 && parameters[1].equals("set")));
            EntityPlayerMP player = parameters.length == 2? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
            StatsServer stats = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getStats();
            if (set) {
                stats.setGold(player, amount);
            } else stats.addGold(player, amount);
        } else throw new WrongUsageException(getUsage(sender));
    }
}
