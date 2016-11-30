package joshie.harvest.plugins.crafttweaker.command;

import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.npc.NPC;
import joshie.harvest.npc.NPCRegistry;
import minetweaker.MineTweakerAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HFCommandNPC extends AbstractHFCommand {

    @Override
    public String getCommandName() {
        return "npclist";
    }

    @Override
    public String getUsage() {
        return "/hf npclist";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandException {
        MineTweakerAPI.logCommand("NPCs: \n" + this.getShopList().toString().replace("[", "").replace("]", "").replace(", ", "\n"));
        sender.addChatMessage(new TextComponentString("List generated; see minetweaker.log in your minecraft dir"));

        return true;
    }

    private List<ResourceLocation> getShopList() {
        List<ResourceLocation> list = new ArrayList<>();
        for (NPC npc: NPCRegistry.REGISTRY.getValues()) {
            list.add(npc.getRegistryName());
            Collections.sort(list, (s1, s2) -> s1.toString().compareTo(s2.toString()));
        }

        return list;
    }
}