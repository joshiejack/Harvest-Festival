package joshie.harvest.plugins.crafttweaker.command;

import joshie.harvest.api.shops.Shop;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import minetweaker.MineTweakerAPI;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class HFCommandShops extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "shops";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf shops";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.ANYONE.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        MineTweakerAPI.logCommand("Shops: \n" + this.getShopList().toString().replace("[", "").replace("]", "").replace(", ", "\n"));
        sender.sendMessage(new TextComponentString("List generated; see minetweaker.log in your minecraft dir"));
    }

    private List<ResourceLocation> getShopList() {
        List<ResourceLocation> list = Shop.REGISTRY.entrySet().stream().map(Entry::getKey).collect(Collectors.toList());
        Collections.sort(list, (s1, s2) -> s1.toString().compareTo(s2.toString()));
        return list;
    }
}