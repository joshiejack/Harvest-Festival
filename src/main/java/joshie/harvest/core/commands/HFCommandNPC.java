package joshie.harvest.core.commands;

import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@HFCommand
public class HFCommandNPC extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "npclist";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        sender.getEntityWorld().loadedEntityList.stream().filter(entity -> entity instanceof EntityNPC).forEach(entity -> {
            EntityNPC npc = (EntityNPC) entity;
            TextComponentString componentTranslation = new TextComponentString(npc.getNPC().getLocalizedName() + " is hiding at the coordinates " + (int) npc.posX + " " + (int) npc.posY + " " + (int) npc.posZ);
            sender.addChatMessage(componentTranslation);
        });

        return true;
    }

    @Override
    public String getUsage() {
        return "";
    }
}