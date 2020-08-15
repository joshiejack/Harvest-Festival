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

import static joshie.harvest.api.calendar.CalendarDate.DAYS_PER_SEASON;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandDay extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "day";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf day <value>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 1) {
            CalendarServer calendar = HFTrackers.getCalendar(sender.getEntityWorld());
            int day = Math.min(DAYS_PER_SEASON, Math.max(1, parseInt(parameters[0]))) - 1;
            Season season = calendar.getDate().getSeason();
            int year = calendar.getDate().getYear() + 1;
            long leftover = server.worlds[0].getWorldTime() % HFCalendar.TICKS_PER_DAY;
            CalendarHelper.setWorldTime(server, CalendarHelper.getTime(day, season, year) + leftover);
        } else throw new WrongUsageException(getUsage(sender));
    }
}