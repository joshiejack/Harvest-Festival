package uk.joshiejack.seasons.command;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import uk.joshiejack.seasons.Season;
import uk.joshiejack.seasons.Seasons;
import uk.joshiejack.seasons.SeasonsConfig;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.Locale;

@PenguinLoader
public class CommandSeason extends CommandTreeSeasons.SeasonsCommand {
    @Nonnull
    @Override
    public String getName() {
        return "season";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        Season season = Season.valueOf(args[0].toUpperCase(Locale.ENGLISH));
        long time = sender.getEntityWorld().getWorldTime();
        Season current = SeasonsSavedData.getWorldData(sender.getEntityWorld()).getSeason();
        long add = ((season.ordinal() - current.ordinal()) %4) * (TimeHelper.TICKS_PER_DAY * (SeasonsConfig.daysPerSeasonMultiplier * Seasons.DAYS_PER_SEASON));
        sender.getEntityWorld().setWorldTime(time + add);
        SeasonsSavedData.getWorldData(sender.getEntityWorld()).recalculate(sender.getEntityWorld(), true);
    }
}
