package joshie.harvest.calendar.command;

import joshie.harvest.api.HFCommand;
import joshie.harvest.api.HFRegister;
import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.CalendarServer;
import joshie.harvest.core.handlers.HFTrackers;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

@HFRegister
public class HFCommandWeather extends HFCommand {
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
                    HFTrackers.<CalendarServer>getCalendar(sender.getEntityWorld()).setTodaysWeather(weather);
                    return true;
                }
            }
        }

        return false;
    }
}