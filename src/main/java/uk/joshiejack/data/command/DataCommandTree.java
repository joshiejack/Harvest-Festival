package uk.joshiejack.data.command;

import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;

@PenguinLoader
@Mod.EventBusSubscriber(modid = "data")
public class DataCommandTree extends CommandTreeBase {
    public static final DataCommandTree INSTANCE = new DataCommandTree();

    @SubscribeEvent
    public static void onCollection(CollectRegistryEvent event) {
        event.add(DataCommand.class, (d, c, s, l) -> INSTANCE.addSubcommand(c.newInstance()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "data";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }
}