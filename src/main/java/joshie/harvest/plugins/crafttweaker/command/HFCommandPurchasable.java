package joshie.harvest.plugins.crafttweaker.command;

import joshie.harvest.api.shops.Shop;
import joshie.harvest.core.commands.AbstractHFCommand;
import minetweaker.MineTweakerAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

public class HFCommandPurchasable extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "items";
    }

    @Override
    public String getUsage() {
        return "/hf items [shopid]";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandException {
        if (parameters.length != 1) return false;
        MineTweakerAPI.logCommand("Items: \n" + Shop.REGISTRY.get(new ResourceLocation(parameters[0])).getPurchasableIDs().toString().replace("[", "").replace("]", "").replace(", ", "\n"));
        sender.addChatMessage(new TextComponentString("List generated; see minetweaker.log in your minecraft dir"));
        return true;
    }
}