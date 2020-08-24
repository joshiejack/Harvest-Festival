package joshie.harvest.mining.command;

import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.mining.MiningHelper;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandMine extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "mine";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf mine [player] <mineID>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 1 || parameters.length == 2) {
            try {
                int mineID = parseInt(parameters[parameters.length - 1]);
                Entity entity;
                if (parameters.length == 1) entity = getCommandSenderAsPlayer(sender);
                else entity = getEntity(server, sender, parameters[0]);
                MiningHelper.teleportToMine(entity, mineID);
            } catch (EntityNotFoundException ex) { /* Do nothing*/ }
        } else throw new WrongUsageException(getUsage(sender));
    }
}