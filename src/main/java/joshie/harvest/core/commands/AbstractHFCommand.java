package joshie.harvest.core.commands;

import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;

public abstract class AbstractHFCommand implements Comparable<Object> {
    /** Return the command name **/
    public abstract String getCommandName();

    /** Return the usage **/
    public abstract String getUsage();

    /** Execute this command
     * @param server        the minecraft server
     * @param sender        the sender
     * @param parameters    additional paramaters
     * @return true if the command was executed
     * @throws CommandNotFoundException
     * @throws NumberInvalidException */
    public abstract boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException;

    /** Return the permission level **/
    public CommandLevel getPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY;
    }

    @Override
    public int compareTo(Object o) {
        return getCommandName().compareTo(((AbstractHFCommand) o).getCommandName());
    }

    /** Helper for parsing integers **/
    protected int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) { return  0; }
    }

    public enum CommandLevel {
        ANYONE, OP_BYPASS_PROTECTION, OP_AFFECT_GAMEPLAY, OP_BAN_PLAYERS, OP_STOP_SERVER
    }
}