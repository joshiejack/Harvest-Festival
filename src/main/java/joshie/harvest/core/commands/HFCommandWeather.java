package joshie.harvest.core.commands;

import joshie.harvest.api.calendar.Weather;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

public class HFCommandWeather extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "weather";
    }

    @Override
    public String getUsage() {
        return "<weather>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            for (Weather weather : Weather.values()) {
                if (StringUtils.equalsIgnoreCase(weather.name(), parameters[0])) {
                    HFTrackers.getCalendar().setTodaysWeather(weather);
                    return true;
                }
            }
        }

        return false;
    }
}