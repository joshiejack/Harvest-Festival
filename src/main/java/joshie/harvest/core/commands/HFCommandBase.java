package joshie.harvest.core.commands;

import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public abstract class HFCommandBase implements Comparable {
    public abstract String getCommandName();

    public abstract boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException;

    public abstract String getUsage();

    public EntityPlayerMP getPlayer(ICommandSender sender) {
        return ((EntityPlayerMP) sender);
    }

    public CommandLevel getPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY;
    }

    @Override
    public int compareTo(Object o) {
        return getCommandName().compareTo(((HFCommandBase) o).getCommandName());
    }
}