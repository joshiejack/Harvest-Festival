package joshie.harvest.calendar.command;

import joshie.harvest.calendar.packet.PacketEdit;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.core.network.PacketHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

@HFCommand
public class HFCommandEdit extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "edit";
    }

    @Override
    public String getUsage() {
        return "/hf edit <gold|date>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1 && sender instanceof EntityPlayer) {
            if (parameters[0].equals("gold")) {
                PacketHandler.sendToClient(new PacketEdit(true), (EntityPlayer) sender);
                return true;
            } else if (parameters[0].equals("date")) {
                PacketHandler.sendToClient(new PacketEdit(false), (EntityPlayer) sender);
                return true;
            }
        }
        return false;
    }
}