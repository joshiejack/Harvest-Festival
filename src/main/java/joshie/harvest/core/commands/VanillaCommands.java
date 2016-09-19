package joshie.harvest.core.commands;

import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.CalendarHelper;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.calendar.HFCalendar;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class VanillaCommands {
    public static boolean isHandled(String name) {
        return name.equals("time") || name.equals("weather");
    }

    public static boolean executeVanillaTime(MinecraftServer server, ICommandSender sender, String[] args) throws NumberInvalidException {
        if (args.length > 1) {
            if (args[0].equals("set")) {
                long time = CalendarHelper.getElapsedDays(server.worldServers[0].getWorldTime()) * HFCalendar.TICKS_PER_DAY;
                if (args[1].equals("force-day")) {
                    time = 1000;
                } else if (args[1].equals("force-night")) {
                    time = 13000;
                } else if (args[1].equals("force")) {
                    time = CommandBase.parseInt(args[1]);
                } else if (args[1].equals("day")) {
                    time += 3000;
                } else if (args[1].equals("night")) {
                    time += 18000;
                } else {
                    time += CommandBase.parseInt(args[1]);
                }


                CalendarHelper.setWorldTime(server, time);
                return true;
            }
        }

        return false;
    }

    public static boolean executeVanillaWeather(MinecraftServer server, ICommandSender sender, String[] args) throws WrongUsageException {
        if (args.length >= 1 && args.length <= 2) {
            if ("clear".equalsIgnoreCase(args[0])) {
                HFTrackers.<CalendarServer>getCalendar(server.worldServers[0]).setTodaysWeather(Weather.SUNNY);
            } else if ("rain".equalsIgnoreCase(args[0])) {
                HFTrackers.<CalendarServer>getCalendar(server.worldServers[0]).setTodaysWeather(Weather.RAIN);
            } else {
                if (!"thunder".equalsIgnoreCase(args[0])) {
                    throw new WrongUsageException("commands.weather.usage");
                }

                HFTrackers.<CalendarServer>getCalendar(server.worldServers[0]).setTodaysWeather(Weather.TYPHOON);
            }

            return true;
        } else {
            throw new WrongUsageException("commands.weather.usage");
        }
    }
}
