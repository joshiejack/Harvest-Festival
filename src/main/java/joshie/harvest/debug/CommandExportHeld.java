package joshie.harvest.debug;

import joshie.harvest.core.commands.AbstractHFCommand;
import net.minecraft.command.CommandNotFoundException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public abstract class CommandExportHeld extends AbstractHFCommand {
    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) throws CommandNotFoundException, NumberInvalidException {
        if (sender instanceof EntityPlayer) {
            ItemStack stack = ((EntityPlayer) sender).getHeldItemMainhand();
            if (stack != null && isExportable(stack)) {
                export(stack, parameters);
            }
        }

        return true;
    }

    protected abstract boolean isExportable(ItemStack stack);
    protected abstract void export(ItemStack stack, String... parameters);

    @Override
    public String getUsage() {
        return "";
    }
}
