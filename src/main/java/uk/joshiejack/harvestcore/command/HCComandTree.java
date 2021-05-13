package uk.joshiejack.harvestcore.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.command.CommandTreeBase;
import uk.joshiejack.harvestcore.HarvestCore;
import uk.joshiejack.penguinlib.events.CollectRegistryEvent;
import uk.joshiejack.penguinlib.util.PenguinLoader;

import javax.annotation.Nonnull;

@PenguinLoader
@Mod.EventBusSubscriber(modid = HarvestCore.MODID)
public class HCComandTree extends CommandTreeBase {
    public static final HCComandTree INSTANCE = new HCComandTree();

    @SubscribeEvent
    public static void onCollection(CollectRegistryEvent event) {
        event.add(HCComand.class, (d, c, s, l) -> INSTANCE.addSubcommand(c.newInstance()));
    }

    @Nonnull
    @Override
    public String getName() {
        return "hc";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }
}
