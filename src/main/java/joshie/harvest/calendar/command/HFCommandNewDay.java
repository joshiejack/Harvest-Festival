package joshie.harvest.calendar.command;

import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

import static joshie.harvest.calendar.HFCalendar.TICKS_PER_DAY;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandNewDay extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "newDay";
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "/hf newDay";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        long i = sender.getEntityWorld().getWorldTime() + TICKS_PER_DAY;
        CalendarHelper.setWorldTime(server, (i - i % TICKS_PER_DAY) - 1);
    }
}