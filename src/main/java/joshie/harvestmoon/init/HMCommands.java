package joshie.harvestmoon.init;

import joshie.harvestmoon.commands.CommandDay;
import joshie.harvestmoon.commands.CommandSeason;
import joshie.harvestmoon.commands.CommandYear;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;

public class HMCommands {
    public static void init(ICommandManager iCommand) {
        if(iCommand instanceof ServerCommandManager) {
            ServerCommandManager manager = ((ServerCommandManager)iCommand);
            manager.registerCommand(new CommandDay());
            manager.registerCommand(new CommandSeason());
            manager.registerCommand(new CommandYear());
        }
    }
}
