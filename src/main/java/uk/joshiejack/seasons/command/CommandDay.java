package uk.joshiejack.seasons.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import uk.joshiejack.seasons.date.DateHelper;
import uk.joshiejack.seasons.world.storage.SeasonsSavedData;

import javax.annotation.Nonnull;

@PenguinLoader
public class CommandDay extends CommandTreeSeasons.SeasonsCommand {
    @Nonnull
    @Override
    public String getName() {
        return "day";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        int day = Integer.parseInt(args[0]);
        long time = sender.getEntityWorld().getWorldTime();
        int current = DateHelper.getDate(sender.getEntityWorld()).getDay();
        long add = (day - current) * TimeHelper.TICKS_PER_DAY; //time_unit > day
        sender.getEntityWorld().setWorldTime(time + add);
        SeasonsSavedData.getWorldData(sender.getEntityWorld()).recalculate(sender.getEntityWorld(), true);
    }
}
