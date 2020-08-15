package joshie.harvest.core.commands;

import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.npcs.entity.EntityNPC;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandNPC extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "npclist";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        sender.getEntityWorld().loadedEntityList.stream().filter(entity -> entity instanceof EntityNPC).forEach(entity -> {
            EntityNPC npc = (EntityNPC) entity;
            TextComponentString componentTranslation = new TextComponentString(npc.getNPC().getLocalizedName() + " is hiding at the coordinates " + (int) npc.posX + " " + (int) npc.posY + " " + (int) npc.posZ);
            sender.sendMessage(componentTranslation);
        });
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_BYPASS_PROTECTION.ordinal();
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf npclist";
    }
}