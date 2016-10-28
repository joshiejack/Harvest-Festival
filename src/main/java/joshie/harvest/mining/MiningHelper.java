package joshie.harvest.mining;

import joshie.harvest.api.calendar.Season;
import joshie.harvest.core.HFTrackers;
import joshie.harvest.core.helpers.EntityHelper;
import joshie.harvest.mining.block.BlockPortal.Portal;
import joshie.harvest.mining.gen.MiningProvider;
import joshie.harvest.town.tracker.TownTracker;
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
import java.util.Random;

import static joshie.harvest.api.calendar.Season.WINTER;
import static joshie.harvest.mining.HFMining.MINING_ID;
import static joshie.harvest.mining.gen.MineManager.CHUNK_BOUNDARY;

public class MiningHelper {
    public static final int MYSTRIL_FLOOR = 150;
    public static final int GOLD_FLOOR = 80;
    public static final int SILVER_FLOOR = 40;
    public static final double WORLD_HEIGHT = 256D;
    public static final int MAX_Y = (int) WORLD_HEIGHT - 1;
    public static final int FLOOR_HEIGHT = 6;
    public static final int MAX_FLOORS = (int) Math.floor(WORLD_HEIGHT / FLOOR_HEIGHT);
    public static final int MAX_LOOP = (int) WORLD_HEIGHT - FLOOR_HEIGHT;
    public static final int CHICK_FLOORS = 7;
    public static final int CHICKEN_FLOORS = 10;
    public static final int SHEEP_FLOORS = 13;
    public static final int COW_FLOORS = 18;

    public static List<ItemStack> getLoot(ResourceLocation loot, World world, EntityPlayer player, float luck) {
        LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer) world);
        lootcontext$builder.withLuck(player.getLuck() + luck);
        lootcontext$builder.withPlayer(player);
        return world.getLootTableManager().getLootTableFromLocation(loot).generateLootForPools(world.rand, lootcontext$builder.build());
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
        Rotation rotation = tracker.getMineOrientation(mineID);
        if (spawn == null) spawn = entity.worldObj.getSpawnPoint();
        if (rotation == Rotation.NONE) {
            entity.rotationYaw = 90F;
        } else if (rotation == Rotation.CLOCKWISE_90) {
            entity.rotationYaw = 180F;
        } else if (rotation == Rotation.CLOCKWISE_180) {
            entity.rotationYaw = 270F;
        } else if (rotation == Rotation.COUNTERCLOCKWISE_90) {
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
        int floorIndex = (int) (MAX_FLOORS - Math.floor(((double)posY) / FLOOR_HEIGHT));
        return (chunkIndex * MAX_FLOORS) + floorIndex; //Floor
    }

    public static int getOreChance(Season season, int floor, Random rand) {
        int minimum = season == WINTER ? 8 : 10;
        int maximum = season == WINTER ? 10: 15;
        if (floor %COW_FLOORS == 0) {
            minimum -= 5;
            maximum -= 5;
        } else if (floor %SHEEP_FLOORS == 0) {
            minimum -= 3;
            maximum -= 3;
        } else if (floor %CHICKEN_FLOORS == 0) {
            minimum -= 2;
            maximum -= 2;
        } else if (floor %CHICK_FLOORS == 0) {
            minimum--;
            maximum--;
        }

        return rand.nextInt(5) == 0 ? minimum + rand.nextInt(maximum) : minimum + 10 + rand.nextInt(maximum);
    }
}
