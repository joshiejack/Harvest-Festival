package joshie.harvest.calendar.command;

import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.HFCalendar;
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
public class HFCommandTime extends CommandBase {
    @Override
    @Nonnull
    public String getName() {
        return "time";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/hf time <set|add> <value>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length > 1) {
            if (parameters[0].equals("set")) {
                long time = CalendarHelper.getElapsedDays(server.worlds[0].getWorldTime()) * HFCalendar.TICKS_PER_DAY;
                switch (parameters[1]) {
                    case "day":
                        time += 3000;
                        break;
                    case "night":
                        time += 18000;
                        break;
                    default:
                        time += (parseInt(parameters[1]) - 6000L);
                        break;
                }

                CalendarHelper.setWorldTime(server, time);
            }

            if (parameters[0].equals("add")) {
                int l = parseInt(parameters[1]);
                CalendarHelper.setWorldTime(server, server.worlds[0].getWorldTime() + l);
            }
        } else throw new WrongUsageException(getUsage(sender));
    }
}