package joshie.harvestmoon.core.commands;

import java.util.List;

import joshie.harvestmoon.core.config.NPC;
import joshie.harvestmoon.core.network.PacketFreeze;
import joshie.harvestmoon.core.network.PacketHandler;
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
        PacketHandler.sendToServer(new PacketFreeze());
        NPC.FREEZE_NPC = !NPC.FREEZE_NPC;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] parameters) {
        return null;
    }
}
