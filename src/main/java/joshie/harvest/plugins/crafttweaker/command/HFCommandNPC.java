package joshie.harvest.plugins.crafttweaker.command;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.commands.AbstractHFCommand;
import minetweaker.MineTweakerAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HFCommandNPC extends AbstractHFCommand {

    @Override
    public String getCommandName() {
        return "npcs";
    }

    @Override
    public String getUsage() {
        return "/hf npcs";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandException {
        MineTweakerAPI.logCommand("NPCs: \n" + this.getShopList().toString().replace("[", "").replace("]", "").replace(", ", "\n"));
        sender.sendMessage(new TextComponentString("List generated; see minetweaker.log in your minecraft dir"));
        return true;
    }

    private List<String> getShopList() {
        List<String> list = NPC.REGISTRY.values().stream().map(npc -> npc.getLocalizedName() + " = " + npc.getResource()).collect(Collectors.toList());
        Collections.sort(list, String::compareTo);
        return list;
    }
}