package joshie.harvest.player.command;

import joshie.harvest.api.knowledge.Note;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.tracking.TrackingServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandNote extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "note";
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "/hf note [player] <value|all|clear>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 1 || parameters.length == 2) {
            EntityPlayerMP player = parameters.length == 1? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
            TrackingServer tracking = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking();
            String note = parameters[parameters.length - 1];
            switch (note) {
                case "all":
                    Note.REGISTRY.values().forEach(tracking::learnNote);
                    tracking.sync(player);
                    break;
                case "clear":
                    tracking.learnNote(null);
                    tracking.sync(player);
                    break;
                default:
                    if (!note.contains(":")) note = "harvestfestival:" + note;
                    tracking.learnNote(Note.REGISTRY.get(new ResourceLocation(note)));
                    tracking.sync(player);
                    break;
            }
        } else throw new WrongUsageException(getCommandUsage(sender));
    }
}
