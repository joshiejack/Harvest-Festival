package joshie.harvest.calendar.command;

import joshie.harvest.api.calendar.Weather;
import joshie.harvest.calendar.data.CalendarServer;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.core.HFTrackers;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.lang3.StringUtils;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandWeather extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "weather";
    }

    @Override
    public String getUsage() {
        return "/hf weather <sunny|rain|snow|typhoon|blizzard>";
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