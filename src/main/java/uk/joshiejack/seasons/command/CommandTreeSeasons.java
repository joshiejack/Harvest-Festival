package uk.joshiejack.seasons.command;

import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.seasons.Seasons;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;

@PenguinLoader
@Mod.EventBusSubscriber(modid = Seasons.MODID)
public class CommandTreeSeasons extends CommandTreeBase {
    public static final CommandTreeSeasons INSTANCE = new CommandTreeSeasons();

    @SubscribeEvent
    public static void onCollection(CollectRegistryEvent event) {
        event.add(SeasonsCommand.class, (d, c, s, l) -> INSTANCE.addSubcommand(c.newInstance()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "seasons";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    abstract static class SeasonsCommand extends CommandBase {}
}
