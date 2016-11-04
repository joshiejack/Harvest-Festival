package joshie.harvest.shops.command;

import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.shops.Shop;
import joshie.harvest.shops.ShopRegistry;
import minetweaker.MineTweakerAPI;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        sender.addChatMessage(new TextComponentString("List generated; see minetweaker.log in your minecraft dir"));

        return true;
    }

    private List<ResourceLocation> getShopList() {
        List<ResourceLocation> list = new ArrayList<>();
        for (Map.Entry<ResourceLocation, Shop> entry : ShopRegistry.INSTANCE.shops.entrySet()) {
            list.add(entry.getKey());
            Collections.sort(list, (s1, s2) -> s1.toString().compareTo(s2.toString()));
        }
        return list;
    }
}