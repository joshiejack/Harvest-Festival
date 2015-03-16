package joshie.harvestmoon.api.commands;

import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public interface IHMCommandHandler extends ICommand {
    public void registerCommand(IHMCommand command);

    public Map getCommands();

    public List getPossibleCommands(ICommandSender sender);
}
