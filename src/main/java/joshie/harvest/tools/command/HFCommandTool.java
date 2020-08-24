package joshie.harvest.tools.command;

import joshie.harvest.api.core.ITiered;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandTool extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "tool";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf tool [player] <value>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    private boolean applyLevel(@Nonnull ItemStack stack, double level) {
        return !(stack.isEmpty() || !(stack.getItem() instanceof ITiered)) && ((ITiered) stack.getItem()).setLevel(stack, level);
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 1 || parameters.length == 2) {
            double level = Double.parseDouble(parameters[parameters.length - 1]);
            EntityPlayerMP player = parameters.length == 1 ? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
            if (!applyLevel(player.getHeldItemOffhand(), level)) applyLevel(player.getHeldItemMainhand(), level);
        } else throw new WrongUsageException(getUsage(sender));
    }
}