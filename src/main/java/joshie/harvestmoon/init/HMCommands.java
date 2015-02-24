package joshie.harvestmoon.init;

import joshie.harvestmoon.core.commands.CommandDay;
import joshie.harvestmoon.core.commands.CommandFreeze;
import joshie.harvestmoon.core.commands.CommandGold;
import joshie.harvestmoon.core.commands.CommandSeason;
import joshie.harvestmoon.core.commands.CommandYear;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;

public class HMCommands {
    public static void init(ICommandManager iCommand) {
        if(iCommand instanceof ServerCommandManager) {
            ServerCommandManager manager = ((ServerCommandManager)iCommand);
            manager.registerCommand(new CommandDay());
            manager.registerCommand(new CommandSeason());
            manager.registerCommand(new CommandYear());
            manager.registerCommand(new CommandGold());
            manager.registerCommand(new CommandFreeze());
        }
    }
}
