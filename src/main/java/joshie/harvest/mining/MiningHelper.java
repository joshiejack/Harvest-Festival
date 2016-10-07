package joshie.harvest.mining;

import joshie.harvest.core.handlers.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.core.util.Direction;
import joshie.harvest.mining.block.BlockPortal.Portal;
import joshie.harvest.town.TownTracker;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.List;

import static joshie.harvest.mining.HFMining.MINING_ID;
import static joshie.harvest.mining.MineManager.CHUNK_BOUNDARY;
import static joshie.harvest.mining.MiningTicker.MAX_FLOORS;

public class MiningHelper {
    public static ItemStack getLoot(ResourceLocation loot, World world, EntityPlayer player, float luck) {
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
        lootcontext$builder.withLuck(player.getLuck() + luck);
        lootcontext$builder.withPlayer(player);
        List<ItemStack> stacks = world.getLootTableManager().getLootTableFromLocation(loot).generateLootForPools(world.rand, lootcontext$builder.build());
        return stacks.size() > 0 ? stacks.get(0) : null;
    }

    public static int getMineID(int chunkZ) {
        return (int)Math.floor(chunkZ / CHUNK_BOUNDARY);
    }

    private static void preloadChunks(WorldServer worldServer, int mineID, int floor) {
        MiningProvider provider = ((MiningProvider)worldServer.provider);
        if (!provider.areCoordinatesGenerated(mineID, floor)) {
            int xStart = (int) Math.floor((floor - 1) / MAX_FLOORS);
            for (int x = xStart * CHUNK_BOUNDARY; x < (xStart * CHUNK_BOUNDARY) + CHUNK_BOUNDARY; x++) {
                for (int z = mineID * CHUNK_BOUNDARY; z < (mineID * CHUNK_BOUNDARY) + CHUNK_BOUNDARY; z++) {
                    worldServer.getChunkProvider().provideChunk(x, z);
                }
            }

            //Force a save
            if (worldServer instanceof WorldServerMulti) {
                ((WorldServerMulti) worldServer).saveAdditionalData();
            }
        }
    }

    private static boolean isSpawnable(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock().isPassable(world, pos);
    }

    private static BlockPos modifySpawnAndPlayerRotation(WorldServer dim, BlockPos spawn, Entity entity) {
        IBlockState actual = HFMining.PORTAL.getActualState(dim.getBlockState(spawn), dim, spawn);
        if (actual.getBlock() == HFMining.PORTAL) {
            Portal portal = HFMining.PORTAL.getEnumFromState(actual);
            for (int distance = 2; distance < 9; distance++) {
                if (portal.isEW()) {
                    if (isSpawnable(dim, spawn.north(distance))) {
                        entity.rotationYaw = 180F;
                        return spawn.north(distance);
                    } else if (isSpawnable(dim, spawn.south(distance))) {
                        entity.rotationYaw = 0F;
                        return spawn.south(distance);
                    }
                } else {
                    if (isSpawnable(dim, spawn.east(distance))) {
                        entity.rotationYaw = 270F;
                        return spawn.east(distance);
                    } else if (isSpawnable(dim, spawn.west(distance))) {
                        entity.rotationYaw = 90F;
                        return spawn.west(distance);
                    }
                }
            }
        }

        return spawn;
    }

    public static boolean teleportToMine(Entity entity) {
        int id = HFTrackers.getTownTracker(entity.worldObj).getMineIDFromCoordinates(new BlockPos(entity));
        return id != -1 && teleportToMine(entity, id);
    }

    public static boolean teleportToMine(Entity entity, int mineID) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer newWorld = server.worldServerForDimension(MINING_ID);
        preloadChunks(newWorld, mineID, 1);
        BlockPos spawn = modifySpawnAndPlayerRotation(newWorld, ((MiningProvider)newWorld.provider).getSpawnCoordinateForMine(mineID, 1), entity);
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

    public static boolean teleportBetweenMine(Entity entity) {
        int mineID = MiningHelper.getMineID((int)entity.posZ >> 4); //Current Mine
        int floor = getFloor((int)entity.posX >> 4, (int) entity.posY); //Current Floor
        boolean top = floor % MAX_FLOORS == 1;
        int newFloor = top ? floor - 1 : floor + 1;
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        WorldServer newWorld = server.worldServerForDimension(MINING_ID);
        preloadChunks(newWorld, mineID, newFloor);
        BlockPos spawn = modifySpawnAndPlayerRotation(newWorld, ((MiningProvider)newWorld.provider).getSpawnCoordinateForMine(mineID, newFloor), entity);
        if (entity.timeUntilPortal == 0) {
            entity.timeUntilPortal = 100;
            if (entity instanceof EntityPlayerMP) {
                ReflectionHelper.setPrivateValue(EntityPlayerMP.class, (EntityPlayerMP) entity, true, "invulnerableDimensionChange", "field_184851_cj");
            }

            entity.setPositionAndUpdate(spawn.getX() + 0.5D, spawn.getY() + 0.1D, spawn.getZ() + 0.5D);
        }

        //Teleport complete
        return true;
    }

    public static int getFloor(int xPosition, int posY) {
        int chunkIndex = (int) Math.floor(((double)xPosition) / CHUNK_BOUNDARY);
        int floorIndex = (int) (MAX_FLOORS - Math.floor(((double)posY) / MiningTicker.FLOOR_HEIGHT));
        return (chunkIndex * MAX_FLOORS) + floorIndex; //Floor
    }
}
