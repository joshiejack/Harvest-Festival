package joshie.harvest.core.commands;

import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;

@HFCommand
public class HFCommandHelp extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "help";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf help";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandManager.CommandLevel.ANYONE.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        List<ICommand> list = CommandManager.INSTANCE.getSortedCommandList();
        int i0 = 7;
        int i = (list.size() - 1) / i0;
        int k;

        try {
            k = parameters.length == 0 ? 0 : net.minecraft.command.CommandBase.parseInt(parameters[0], 1, i + 1) - 1;
        } catch (NumberInvalidException numberinvalidexception) {
            Map<String, ICommand> map = CommandManager.INSTANCE.getCommandMap();
            ICommand icommand = map.get(parameters[0]);
            if (icommand != null) {
                throw new WrongUsageException(icommand.getUsage(sender));
            }

            if (MathHelper.getInt(parameters[0], -1) != -1) {
                throw numberinvalidexception;
            }

            throw new CommandNotFoundException();
        }

        int j = Math.min((k + 1) * i0, list.size());
        TextComponentTranslation componentTranslation1 = new TextComponentTranslation("hf.commands.help.header", k + 1, i + 1);
        componentTranslation1.getStyle().setColor(TextFormatting.DARK_GREEN);
        sender.sendMessage(componentTranslation1);

        for (int l = k * i0; l < j; ++l) {
            ICommand icommand1 = list.get(l);
            TextComponentTranslation componentTranslation = new TextComponentTranslation(icommand1.getUsage(sender));
            componentTranslation.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getName() + " "));
            sender.sendMessage(componentTranslation);
        }
    }
}