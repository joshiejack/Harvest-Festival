package joshie.harvest.mining.command;

import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.mining.MiningHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.EntityNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandMine extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "mine";
    }

    @Override
    public String getUsage() {
        return "/hf mine [player] <mineID>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandException {
        if (parameters.length == 1 || parameters.length == 2) {
            try {
                int mineID = parseInt(parameters[parameters.length - 1]);
                Entity entity;
                if (parameters.length == 1) entity = CommandBase.getCommandSenderAsPlayer(sender);
                else entity = CommandBase.getEntity(server, sender, parameters[0]);
                return MiningHelper.teleportToMine(entity, mineID);
            } catch (EntityNotFoundException ex) { /* Do nothing*/ }
        }

        return false;
    }
}