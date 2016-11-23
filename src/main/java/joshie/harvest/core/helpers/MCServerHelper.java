package joshie.harvest.core.helpers;

import joshie.harvest.core.base.tile.TileHarvest;
import joshie.harvest.core.network.PacketHandler;
import joshie.harvest.core.network.PacketRefresh;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class MCServerHelper {
    public static World getWorld(int dimension) {
        return DimensionManager.getWorld(dimension);
    }

    public static void markForUpdate(World world, BlockPos pos) {
        markForUpdate(world, pos, world.getBlockState(pos));
    }

    private static void markForUpdate(World world, BlockPos pos, IBlockState state) {
        markForUpdate(world, pos, state, 2);
    }

    private static void markForUpdate(World world, BlockPos pos, IBlockState state, int value) {
        world.notifyBlockUpdate(pos, state, state, value);
    }

    public static void markForUpdate(World world, BlockPos pos, int value) {
        markForUpdate(world, pos, world.getBlockState(pos), value);
    }

    public static void markTileForUpdate(TileHarvest tile) {
        PacketRefresh packet = new PacketRefresh(tile.getPos(), tile.getUpdateTag());
        if (tile.getWorld() instanceof WorldServer) {
            WorldServer server = (WorldServer)tile.getWorld();
            BlockPos pos = tile.getPos();
            for (EntityPlayer player : server.playerEntities) {
                EntityPlayerMP mp = ((EntityPlayerMP) player);
                if (mp.getDistanceSq(pos) < 64 * 64 && server.getPlayerChunkMap().isPlayerWatchingChunk(mp, pos.getX() >> 4, pos.getZ() >> 4)) {
                    PacketHandler.sendToClient(packet, mp);
                }
            }
        }
    }
}
