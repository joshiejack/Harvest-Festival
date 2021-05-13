package uk.joshiejack.seasons.command;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;
import uk.joshiejack.seasons.world.weather.Weather;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.Locale;

@PenguinLoader
public class CommandWeather extends CommandTreeSeasons.SeasonsCommand {
    @Nonnull
    @Override
    public String getName() {
        return "weather";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        if (args.length > 0) {
            Weather type = Weather.valueOf(args[0].toUpperCase(Locale.ENGLISH));
            boolean today = args.length == 1 || !args[1].equals("tomorrow");
            SeasonsSavedData.getWorldData(sender.getEntityWorld()).changeWeather(type, today);
            sender.getEntityWorld().provider.calculateInitialWeather();
        }
    }
}
