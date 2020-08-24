package joshie.harvest.calendar.command;

import joshie.harvest.calendar.packet.PacketEdit;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandEdit extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "edit";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf edit <gold|date>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.ANYONE.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 1 && sender instanceof EntityPlayer) {
            if (parameters[0].equals("gold")) {
                PacketHandler.sendToClient(new PacketEdit(true), (EntityPlayer) sender);
            } else if (parameters[0].equals("date")) {
                PacketHandler.sendToClient(new PacketEdit(false), (EntityPlayer) sender);
            }
        } else throw new WrongUsageException(getUsage(sender));
    }
}