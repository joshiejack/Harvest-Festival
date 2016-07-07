package joshie.harvest.core.commands;

import joshie.harvest.mining.MiningProvider;
import joshie.harvest.mining.MiningTeleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import static joshie.harvest.core.config.General.MINING_ID;

public class HFCommandMine extends HFCommandBase {
    @Override
    public String getCommandName() {
        return "mine";
    }

    @Override
    public String getUsage() {
        return "mineID";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        int mineID = 0;
        if (parameters.length == 1) {
            mineID = parseInt(parameters[0]);
        }

        WorldServer worldServer = server.worldServerForDimension(MINING_ID);
        World world = sender.getEntityWorld();
        int oldDimension = world.provider.getDimension();
        if (world != null && sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = ((EntityPlayerMP)sender);
            player.addExperienceLevel(0); //Fix levels
            BlockPos spawn = ((MiningProvider)worldServer.provider).getSpawnCoordinateForMine(mineID);
            worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension((player), MINING_ID, new MiningTeleporter(worldServer, spawn));
            spawn = ((MiningProvider)worldServer.provider).getSpawnCoordinateForMine(mineID); //Reload the data
            player.setPositionAndUpdate(spawn.getX(), spawn.getY(), spawn.getZ());
            if (oldDimension == 1) {
                player.setPositionAndUpdate(spawn.getX(), spawn.getY(), spawn.getZ());
                world.spawnEntityInWorld(player);
                world.updateEntityWithOptionalForce(player, false);
            }

            return true;
        }

        return false;
    }
}