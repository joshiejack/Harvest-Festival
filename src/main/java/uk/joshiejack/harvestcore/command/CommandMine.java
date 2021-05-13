package uk.joshiejack.harvestcore.command;

import uk.joshiejack.harvestcore.world.mine.Mine;
import uk.joshiejack.harvestcore.world.mine.dimension.MineTeleporter;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import javax.annotation.Nonnull;

@PenguinLoader
public class CommandMine extends HCComand {
    @Nonnull
    @Override
    public String getName() {
        return "mine";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (sender instanceof EntityPlayer) {
            if (args.length == 1 || args.length == 2) {
                try {

                    int dim = Mine.BY_NAME.getInt(args[0]);
                    if (dim != 0) {
                        int id = args.length == 2 ? Integer.parseInt(args[1]) : 0;
                        ((EntityPlayer) sender).changeDimension(dim, new MineTeleporter(Mine.BY_ID.get(dim), id));
                    }
                } catch (NumberFormatException ex) {
                    throw new CommandException("Didn't use a valid number for the mine command");
                }
            }
        }
    }
}
