package uk.joshiejack.penguinlib.command;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.minecraft.TimeHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nonnull;

@PenguinLoader
public class CommandNewDay extends PenguinCommand {
    @Nonnull
    @Override
    public String getName() {
        return "newDay";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        sender.getEntityWorld().setWorldTime((TimeHelper.getElapsedDays(sender.getEntityWorld()) + 1) * TimeHelper.TICKS_PER_DAY);
        if (args.length == 1) MinecraftForge.EVENT_BUS.register(new NewdayUpdater(Integer.parseInt(args[0]) - 1));
    }

    private static final class NewdayUpdater {
        private final int days;
        private int counter;

        NewdayUpdater(int days) {
            this.days = days;
        }

        @SubscribeEvent
        public void onUpdate(TickEvent.WorldTickEvent event) {
            if (event.phase == TickEvent.Phase.END && event.world.getWorldTime() %TimeHelper.TICKS_PER_DAY == 20) {
                event.world.setWorldTime((TimeHelper.getElapsedDays(event.world) + 1) * TimeHelper.TICKS_PER_DAY); //New day
                counter++;

                if (counter >= days) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                }
            }
        }
    }
}
