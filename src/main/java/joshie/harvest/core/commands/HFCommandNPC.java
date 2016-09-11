package joshie.harvest.core.commands;

import joshie.harvest.npc.entity.EntityNPC;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

@HFCommand
public class HFCommandNPC extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "npc";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
       for (Entity entity: sender.getEntityWorld().loadedEntityList) {
           if (entity instanceof EntityNPC) {
               EntityNPC npc = (EntityNPC) entity;
               TextComponentString componentTranslation = new TextComponentString(npc.getNPC().getLocalizedName() + " is hiding at the coordinates " + (int)npc.posX + " " + (int)npc.posY + " " + (int)npc.posZ);
               sender.addChatMessage(componentTranslation);
           }
       }

        return true;
    }

    @Override
    public String getUsage() {
        return "";
    }
}