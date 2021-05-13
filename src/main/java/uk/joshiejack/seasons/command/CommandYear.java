package uk.joshiejack.seasons.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.seasons.date.DateHelper;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;

import javax.annotation.Nonnull;

@PenguinLoader
public class CommandYear extends CommandTreeSeasons.SeasonsCommand {
    @Nonnull
    @Override
    public String getName() {
        return "year";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        int year = Integer.parseInt(args[0]);
        long time = sender.getEntityWorld().getWorldTime();
        int current = DateHelper.getDate(sender.getEntityWorld()).getYear();
        long add = (year - current) * 2880000L; //time_unit > year
        sender.getEntityWorld().setWorldTime(time + add);
        SeasonsSavedData.getWorldData(sender.getEntityWorld()).recalculate(sender.getEntityWorld(), true);
    }
}
