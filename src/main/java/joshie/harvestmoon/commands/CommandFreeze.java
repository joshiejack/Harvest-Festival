package joshie.harvestmoon.commands;

import java.util.List;

import joshie.harvestmoon.config.NPC;
import joshie.harvestmoon.network.PacketFreeze;
import joshie.harvestmoon.network.PacketHandler;
import net.minecraft.command.ICommandSender;

public class CommandFreeze extends CommandBase {
    @Override
    public String getCommandName() {
        return "freeze";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/freeze";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parameters) {
        if (parameters == null || parameters.length != 1) return;
        try {
            PacketHandler.sendToServer(new PacketFreeze());
            NPC.FREEZE_NPC = !NPC.FREEZE_NPC;
        } catch (NumberFormatException e) {}
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        return null;
    }
}
