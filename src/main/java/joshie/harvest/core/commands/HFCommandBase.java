package joshie.harvest.core.commands;

import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public abstract class HFCommandBase implements Comparable<Object> {
    public abstract String getCommandName();

    public abstract boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException;

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

    protected int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) { return  0; }
    }
}