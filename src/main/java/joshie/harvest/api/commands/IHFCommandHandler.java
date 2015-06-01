package joshie.harvest.api.commands;

import java.util.List;
import java.util.Map;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;

public interface IHFCommandHandler extends ICommand {
    public void registerCommand(IHFCommand command);

    public Map getCommands();

    public List getPossibleCommands(ICommandSender sender);
}
