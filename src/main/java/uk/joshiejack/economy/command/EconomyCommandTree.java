package uk.joshiejack.economy.command;

import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;

import static uk.joshiejack.economy.Economy.MODID;

@PenguinLoader
@Mod.EventBusSubscriber(modid = MODID)
public class EconomyCommandTree extends CommandTreeBase {
    public static final EconomyCommandTree INSTANCE = new EconomyCommandTree();

    @SubscribeEvent
    public static void onCollection(CollectRegistryEvent event) {
        event.add(EconomyCommand.class, (d, c, s, l) -> INSTANCE.addSubcommand(c.newInstance()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "economy";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }
}
