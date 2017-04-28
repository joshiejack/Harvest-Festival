package joshie.harvest.core.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

public abstract class AbstractHFCommand implements Comparable<Object> {
    /** Return the command name **/
    public abstract String getCommandName();

    /** Return the usage **/
    public String getUsage() {
        return "/hf " + getCommandName();
    }

    /** Execute this command
     * @param server        the Minecraft server
     * @param sender        the sender
     * @param parameters    additional parameters
     * @return true if the command was executed
     * @throws CommandNotFoundException thrown when command was not found
     * @throws NumberInvalidException thrown when a invalid number is parsed */
    public abstract boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandException;

    /** Return the permission level **/
    public CommandLevel getPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY;
    }

    @Override
    public int compareTo(@Nonnull Object o) {
        return getCommandName().compareTo(((AbstractHFCommand) o).getCommandName());
    }

    /** Helper for parsing integers **/
    protected int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        } catch (Exception e) { return  0; }
    }

    enum CommandLevel {
        ANYONE, OP_BYPASS_PROTECTION, OP_AFFECT_GAMEPLAY, OP_BAN_PLAYERS, OP_STOP_SERVER
    }
}