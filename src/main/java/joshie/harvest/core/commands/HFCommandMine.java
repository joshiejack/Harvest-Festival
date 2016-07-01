package joshie.harvest.core.commands;

import joshie.harvest.mining.MiningTeleporter;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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
        return "";
    }

    @Override
    public boolean execute(MinecraftServer server, ICommandSender sender, String[] parameters) {
        WorldServer worldServer = server.worldServerForDimension(MINING_ID);
        World world = sender.getEntityWorld();
        int oldDimension = world.provider.getDimension();
        if (sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = ((EntityPlayerMP)sender);
            player.addExperienceLevel(0); //Fix levels
            worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension((player), MINING_ID, new MiningTeleporter(worldServer, world.provider.getSpawnCoordinate()));

            if (player != null) {
                player.setPositionAndUpdate(world.provider.getSpawnCoordinate().getX(), world.provider.getSpawnCoordinate().getY(), world.provider.getSpawnCoordinate().getZ());
                if (oldDimension == 1) {
                    player.setPositionAndUpdate(world.provider.getSpawnCoordinate().getX(), world.provider.getSpawnCoordinate().getY(), world.provider.getSpawnCoordinate().getZ());
                    world.spawnEntityInWorld(player);
                    world.updateEntityWithOptionalForce(player, false);
                }
            }

            return true;
        }

        return false;
    }
}