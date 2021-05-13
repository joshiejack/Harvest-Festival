package uk.joshiejack.settlements.command;

import uk.joshiejack.settlements.Settlements;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;

@PenguinLoader
@Mod.EventBusSubscriber(modid = Settlements.MODID)
public class AdventureCommandTree extends CommandTreeBase {
    public static final AdventureCommandTree INSTANCE = new AdventureCommandTree();

    @SubscribeEvent
    public static void onCollection(CollectRegistryEvent event) {
        event.add(AdventureCommand.class, (d, c, s, l) -> INSTANCE.addSubcommand(c.newInstance()));
    }

    @Nonnull
    @Override
    public String getName() {
        return Settlements.MODID;
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }
}
