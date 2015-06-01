package joshie.harvest.core.commands;

import joshie.harvest.api.commands.CommandLevel;
import joshie.harvest.api.commands.IHFCommand;

public abstract class HFCommandBase implements IHFCommand {
    @Override
    public CommandLevel getPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY;
    }
    
    @Override
    public int compareTo(Object o) {
        return getCommandName().compareTo(((IHFCommand)o).getCommandName());
    }
}
