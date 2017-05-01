package joshie.harvest.calendar.command;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.calendar.data.CalendarServer;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandYear extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "year";
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "/hf year <value>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 1) {
            CalendarServer calendar = HFTrackers.getCalendar(sender.getEntityWorld());
            int day = calendar.getDate().getDay();
            Season season = calendar.getDate().getSeason();
            int year = Math.min(Integer.MAX_VALUE, Math.max(1, Integer.parseInt(parameters[0])));
            long leftover = server.worldServers[0].getWorldTime() % HFCalendar.TICKS_PER_DAY;
            CalendarHelper.setWorldTime(server, CalendarHelper.getTime(day, season, year) + leftover);
        } else throw new WrongUsageException(getCommandUsage(sender));
    }
}