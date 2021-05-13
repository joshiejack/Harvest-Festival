package uk.joshiejack.penguinlib.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import uk.joshiejack.penguinlib.events.PenguinReloadEvent;
import uk.joshiejack.penguinlib.scripting.Scripting;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;
import java.util.ArrayList;

@PenguinLoader
public class CommandReload extends PenguinCommand {
    @Nonnull
    @Override
    public String getName() {
        return "reload";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) {
        Scripting.reload();
        MinecraftForge.EVENT_BUS.post(new PenguinReloadEvent(sender.getEntityWorld()));
    }
}
