package uk.joshiejack.data.command;

import com.google.common.collect.Maps;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;
import java.util.Map;

@PenguinLoader
public class DataCommandExport extends DataCommand {
    public static final Map<String, Runnable> EXPORTERS = Maps.newHashMap();

    @Override
    public String getName() {
        return "export";
    }

    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "export / npc / recipe / shipping";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) {
        if (args.length == 1) {
            Runnable r = EXPORTERS.get(args[0]);
            if (r != null) {
                r.run();
            }
        }
    }
}
