package joshie.harvest.player.command;

import joshie.harvest.api.cooking.Recipe;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.commands.CommandManager.CommandLevel;
import joshie.harvest.core.commands.HFCommand;
import joshie.harvest.player.PlayerTrackerServer;
import joshie.harvest.player.tracking.TrackingServer;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

@HFCommand
@SuppressWarnings("unused")
public class HFCommandRecipe extends CommandBase {
    @Override
    @Nonnull
    public String getCommandName() {
        return "recipe";
    }

    @Override
    @Nonnull
    public String getCommandUsage(@Nonnull ICommandSender sender) {
        return "/hf recipe [player] <value|all|clear>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return CommandLevel.OP_AFFECT_GAMEPLAY.ordinal();
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] parameters) throws CommandException {
        if (parameters.length == 1 || parameters.length == 2) {
            try {
                EntityPlayerMP player = parameters.length == 1? CommandBase.getCommandSenderAsPlayer(sender) : CommandBase.getPlayer(server, sender, parameters[0]);
                TrackingServer tracking = HFTrackers.<PlayerTrackerServer>getPlayerTrackerFromPlayer(player).getTracking();
                String recipe = parameters[parameters.length - 1];
                switch (recipe) {
                    case "all":
                        Recipe.REGISTRY.values().forEach(tracking::learnRecipe);
                        tracking.sync(player);
                        break;
                    case "clear":
                        tracking.learnRecipe(null);
                        tracking.sync(player);
                        break;
                    default:
                        if (!recipe.contains(":")) recipe = "harvestfestival:" + recipe;
                        tracking.learnRecipe(Recipe.REGISTRY.get(new ResourceLocation(recipe)));
                        tracking.sync(player);
                        break;
                }
            } catch (NumberFormatException | PlayerNotFoundException ignored) {}
        } else throw new WrongUsageException(getCommandUsage(sender));
    }
}
