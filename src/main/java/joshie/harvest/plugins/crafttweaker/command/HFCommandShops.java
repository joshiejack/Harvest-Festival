package joshie.harvest.plugins.crafttweaker.command;

import joshie.harvest.api.shops.Shop;
import joshie.harvest.core.commands.AbstractHFCommand;
import minetweaker.MineTweakerAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class HFCommandShops extends AbstractHFCommand {

    @Override
    public String getCommandName() {
        return "shops";
    }

    @Override
    public String getUsage() {
        return "/hf shops";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandException {
        MineTweakerAPI.logCommand("Shops: \n" + this.getShopList().toString().replace("[", "").replace("]", "").replace(", ", "\n"));
        sender.sendMessage(new TextComponentString("List generated; see minetweaker.log in your minecraft dir"));
        return true;
    }

    private List<ResourceLocation> getShopList() {
        List<ResourceLocation> list = Shop.REGISTRY.entrySet().stream().map(Entry::getKey).collect(Collectors.toList());
        Collections.sort(list, Comparator.comparing(ResourceLocation::toString));
        return list;
    }
}