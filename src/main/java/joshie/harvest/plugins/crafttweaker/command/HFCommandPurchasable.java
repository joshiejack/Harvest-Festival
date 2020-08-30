package joshie.harvest.plugins.crafttweaker.command;

import crafttweaker.CraftTweakerAPI;
import joshie.harvest.api.shops.Shop;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

public class HFCommandPurchasable extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "items";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf items [shopid]";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.ANYONE.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length != 1) throw new WrongUsageException(getUsage(sender));
        CraftTweakerAPI.logCommand("Items: \n" + Shop.REGISTRY.get(new ResourceLocation(parameters[0])).getPurchasableIDs().toString().replace("[", "").replace("]", "").replace(", ", "\n"));
        sender.sendMessage(new TextComponentString("List generated; see minetweaker.log in your minecraft dir"));
    }
}