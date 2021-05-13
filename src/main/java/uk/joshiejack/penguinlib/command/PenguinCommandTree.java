package uk.joshiejack.penguinlib.command;

import uk.joshiejack.penguinlib.PenguinLib;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;

@PenguinLoader
@Mod.EventBusSubscriber(modid = PenguinLib.MOD_ID)
public class PenguinCommandTree extends CommandTreeBase {
    public static final PenguinCommandTree INSTANCE = new PenguinCommandTree();

    @SubscribeEvent
    public static void onCollection(CollectRegistryEvent event) {
        event.add(PenguinCommand.class, (d, c, s, l) -> INSTANCE.addSubcommand(c.newInstance()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "penguin";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }
}
