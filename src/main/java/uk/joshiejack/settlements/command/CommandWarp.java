package uk.joshiejack.settlements.command;

import uk.joshiejack.settlements.util.TownFinder;
import uk.joshiejack.settlements.world.town.Town;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;

@PenguinLoader
public class CommandWarp extends AdventureCommand {
    @Nonnull
    @Override
    public String getName() {
        return "warp";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 2) {
            String waypoint = args[1];
            Town<?> town = TownFinder.find(sender.getEntityWorld(), sender.getPosition());
            Pair<BlockPos, Rotation> warp = town.getLandRegistry().getWaypoint(waypoint);
            Entity entity = getEntity(server, sender, args[0]);
            boolean solid = entity.world.getBlockState(warp.getKey().down()).isSideSolid(entity.world, warp.getKey(), EnumFacing.UP);
            if (!warp.getKey().equals(town.getCentre())) {
                entity.setLocationAndAngles(warp.getKey().getX() + 0.5, warp.getKey().getY() + (solid ? 0 : 0.5), warp.getKey().getZ() + 0.5, warp.getValue().rotate(EnumFacing.EAST).getHorizontalAngle(), 0F);
            }
        }
    }
}
