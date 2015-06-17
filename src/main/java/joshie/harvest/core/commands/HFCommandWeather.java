package joshie.harvest.core.commands;

import joshie.harvest.api.calendar.Weather;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketSetWeather;
import net.minecraft.command.ICommandSender;

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
    public boolean processCommand(ICommandSender sender, String[] parameters) {
        if (parameters != null && parameters.length == 1) {
            for (Weather w : Weather.values()) {
                if (StringUtils.equalsIgnoreCase(w.name(), parameters[0])) {
                    PacketHandler.sendToServer(new PacketSetWeather(w));
                    return true;
                }
            }
        }

        return false;
    }
}
