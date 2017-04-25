package joshie.harvest.core.commands;

import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@HFCommand
public class HFCommandHelp extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "help";
    }

    @Override
    public CommandLevel getPermissionLevel() {
        return CommandLevel.ANYONE;
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        List<AbstractHFCommand> list = this.getSortedPossibleCommands(sender);
        int i0 = 7;
        int i = (list.size() - 1) / i0;
        int k;

        try {
            k = parameters.length == 0 ? 0 : net.minecraft.command.CommandBase.parseInt(parameters[0], 1, i + 1) - 1;
        } catch (NumberInvalidException numberinvalidexception) {
            Map<String, AbstractHFCommand> map = this.getCommands();
            AbstractHFCommand icommand = map.get(parameters[0]);

            if (icommand != null) {
                CommandManager.throwError(sender, icommand);
                return true;
            }

            if (MathHelper.parseIntWithDefault(parameters[0], -1) != -1) {
                throw numberinvalidexception;
            }

            throw new CommandNotFoundException();
        }

        int j = Math.min((k + 1) * i0, list.size());
        TextComponentTranslation componentTranslation1 = new TextComponentTranslation("hf.commands.help.header", k + 1, i + 1);
        componentTranslation1.getStyle().setColor(TextFormatting.DARK_GREEN);
        sender.sendMessage(componentTranslation1);

        for (int l = k * i0; l < j; ++l) {
            AbstractHFCommand icommand1 = list.get(l);
            TextComponentTranslation componentTranslation = new TextComponentTranslation(CommandManager.getUsage(icommand1));
            componentTranslation.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/" + icommand1.getCommandName() + " "));
            sender.sendMessage(componentTranslation);
        }

        return true;
    }

    private Map<String, AbstractHFCommand> getCommands() {
        return CommandManager.INSTANCE.getCommands();
    }

    private List<AbstractHFCommand> getSortedPossibleCommands(ICommandSender sender) {
        List<AbstractHFCommand> list = CommandManager.INSTANCE.getPossibleCommands(sender);
        Collections.sort(list);
        return list;
    }

    @Override
    public String getUsage() {
        return "/hf help";
    }
}