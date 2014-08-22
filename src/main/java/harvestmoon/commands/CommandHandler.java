package harvestmoon.commands;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;

public class CommandHandler {
    public static void init(ICommandManager iCommand) {
        if(iCommand instanceof ServerCommandManager) {
            ServerCommandManager manager = ((ServerCommandManager)iCommand);
            manager.registerCommand(new CommandDay());
            manager.registerCommand(new CommandSeason());
            manager.registerCommand(new CommandYear());
        }
    }
}
