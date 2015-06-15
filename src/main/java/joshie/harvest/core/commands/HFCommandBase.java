package joshie.harvest.core.commands;

import net.minecraft.command.ICommandSender;

public abstract class HFCommandBase implements Comparable {
    public abstract String getCommandName();
    public abstract boolean processCommand(ICommandSender sender, String[] parameters);
    public abstract String getUsage();
    
    public CommandLevel getPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY;
    }
    
    @Override
    public int compareTo(Object o) {
        return getCommandName().compareTo(((HFCommandBase)o).getCommandName());
    }
    
    public void execute(String[] parameters) {};
}
