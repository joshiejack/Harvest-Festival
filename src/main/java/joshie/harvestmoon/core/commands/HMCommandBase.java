package joshie.harvestmoon.core.commands;

import joshie.harvestmoon.api.commands.CommandLevel;
import joshie.harvestmoon.api.commands.IHMCommand;

public abstract class HMCommandBase implements IHMCommand {
    @Override
    public CommandLevel getPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY;
    }
    
    @Override
    public int compareTo(Object o) {
        return getCommandName().compareTo(((IHMCommand)o).getCommandName());
    }
}
