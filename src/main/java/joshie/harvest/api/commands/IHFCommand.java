package joshie.harvest.api.commands;

import net.minecraft.command.ICommandSender;

/** This is used for adding commands **/
public interface IHFCommand extends Comparable {
    public String getCommandName();

    public CommandLevel getPermissionLevel();

    /** Returns true if the command was successfully processed **/
    public boolean processCommand(ICommandSender sender, String[] parameters);

    public String getUsage();
}
