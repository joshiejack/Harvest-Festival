package joshie.harvest.player.command;

import joshie.harvest.api.knowledge.Note;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.AbstractHFCommand;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.tracking.TrackingServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandNote extends AbstractHFCommand {
    @Override
    public String getCommandName() {
        return "note";
    }

    @Override
    public String getUsage() {
        return "/hf note [player] <value|all|clear>";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        if (parameters != null && (parameters.length == 1 || parameters.length == 2)) {
            try {
                EntityPlayerMP player = parameters.length == 1? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
                TrackingServer tracking = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking();
                String note = parameters[parameters.length - 1];
                switch (note) {
                    case "all":
                        Note.REGISTRY.values().forEach(tracking::learnNote);
                        tracking.sync(player);
                        return true;
                    case "clear":
                        tracking.learnNote(null);
                        tracking.sync(player);
                        return true;
                    default:
                        if (!note.contains(":")) note = "harvestfestival:" + note;
                        tracking.learnNote(Note.REGISTRY.get(new ResourceLocation(note)));
                        tracking.sync(player);
                        return true;
                }
            } catch (NumberFormatException | PlayerNotFoundException ignored) {}
        }

        return false;
    }
}
