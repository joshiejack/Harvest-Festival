package joshie.harvest.mining;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.generic.EntityHelper;
import joshie.harvest.core.util.Direction;
import joshie.harvest.mining.blocks.BlockPortal.Portal;
import joshie.harvest.town.TownTracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.FMLCommonHandler;

import static joshie.harvest.mining.HFMining.MINING_ID;
import static joshie.harvest.mining.MineManager.CHUNK_BOUNDARY;

public class MiningHelper {
    public static ItemStack getLoot(ResourceLocation loot, World world, EntityPlayer player, float luck) {
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
        lootcontext$builder.withLuck(player.getLuck() + luck);
        lootcontext$builder.withPlayer(player);
        for (ItemStack itemstack : world.getLootTableManager().getLootTableFromLocation(loot).generateLootForPools(world.rand, lootcontext$builder.build())) {
            return itemstack;
        }

        return null;
    }

    private static boolean isSpawnable(World world, BlockPos pos) {
        return world.isAirBlock(pos) || world.getBlockState(pos).getBlock() == HFMining.ORE;
    }

    public static int getMineID(int chunkZ) {
        return (int)Math.floor(chunkZ / CHUNK_BOUNDARY);
    }

    private static void preloadChunks(WorldServer worldServer, int mineID) {
        BlockPos check = ((MiningProvider)worldServer.provider).getSpawnCoordinateForMine(mineID);
        if (check.equals(new BlockPos(0, 254, mineID * CHUNK_BOUNDARY * 16))) { //If the result is the default, then load the chunks
            for (int x = 0; x < CHUNK_BOUNDARY; x++) {
                for (int z = mineID * CHUNK_BOUNDARY; z < (mineID * CHUNK_BOUNDARY) + CHUNK_BOUNDARY; z++) {
                    worldServer.getChunkProvider().provideChunk(x, z);
                }
            }

            //Force a save
            if (worldServer instanceof WorldServerMulti) {
                ((WorldServerMulti)worldServer).saveAdditionalData();
            }
        }
    }

    private static BlockPos modifySpawnAndPlayerRotation(WorldServer dim, BlockPos spawn, Entity entity) {
        IBlockState actual = dim.getBlockState(spawn).getActualState(dim, spawn);
        if (actual.getBlock() == HFMining.PORTAL) {
            Portal portal = HFMining.PORTAL.getEnumFromState(actual);
            if (portal.isEW()) {
                if (isSpawnable(dim, spawn.north(2))) {
                    spawn = spawn.north(2);
                    entity.rotationYaw = 180F;
                } else if (isSpawnable(dim, spawn.south(2))) {
                    spawn = spawn.south(2);
                    entity.rotationYaw = 0F;
                }
            } else {
                if (isSpawnable(dim, spawn.east(2))) {
                    spawn = spawn.east(2);
                    entity.rotationYaw = 270F;
                } else if (isSpawnable(dim, spawn.west(2))) {
                    spawn = spawn.west(2);
                    entity.rotationYaw = 90F;
                }
            }
        }

        return spawn;
    }

    public static boolean teleportToMine(Entity entity) {
        return teleportToMine(entity, HFTrackers.getTownTracker(entity.worldObj).getMineIDFromCoordinates(new BlockPos(entity)));
    }

    public static boolean teleportToMine(Entity entity, int mineID) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer newWorld = server.worldServerForDimension(MINING_ID);
        preloadChunks(newWorld, mineID);
        BlockPos spawn = modifySpawnAndPlayerRotation(newWorld, ((MiningProvider)newWorld.provider).getSpawnCoordinateForMine(mineID), entity);
        return EntityHelper.teleport(entity, MINING_ID, spawn);
    }

    public static boolean teleportToOverworld(Entity entity) {
        int mineID = getMineID((int)entity.posZ >> 4);
        TownTracker tracker = HFTrackers.getTownTracker(DimensionManager.getWorld(0));
        BlockPos spawn = tracker.getCoordinatesForOverworldMine(entity, mineID);
        Direction direction = tracker.getMineOrientation(mineID);
        if (spawn == null) spawn = entity.worldObj.getSpawnPoint();
        if (direction.getRotation() == Rotation.NONE) {
            entity.rotationYaw = 90F;
        } else if (direction.getRotation() == Rotation.CLOCKWISE_90) {
            entity.rotationYaw = 180F;
        } else if (direction.getRotation() == Rotation.CLOCKWISE_180) {
            entity.rotationYaw = 270F;
        } else if (direction.getRotation() == Rotation.COUNTERCLOCKWISE_90) {
            entity.rotationYaw = 0F;
        }

        return EntityHelper.teleport(entity, 0, spawn);
    }
}
