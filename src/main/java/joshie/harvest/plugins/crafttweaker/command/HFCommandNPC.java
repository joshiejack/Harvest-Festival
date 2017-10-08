package joshie.harvest.plugins.crafttweaker.command;

import joshie.harvest.api.npc.NPC;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import minetweaker.MineTweakerAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HFCommandNPC extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "npcs";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf npcs";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.ANYONE.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        MineTweakerAPI.logCommand("NPCs: \n" + this.getShopList().toString().replace("[", "").replace("]", "").replace(", ", "\n"));
        sender.sendMessage(new TextComponentString("List generated; see minetweaker.log in your minecraft dir"));
    }

    private List<String> getShopList() {
        List<String> list = NPC.REGISTRY.values().stream().map(npc -> npc.getLocalizedName() + " = " + npc.getResource()).collect(Collectors.toList());
        Collections.sort(list, String::compareTo);
        return list;
    }
}